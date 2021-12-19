package io.security.corespringsecurity.security.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import io.security.corespringsecurity.security.aop.CustomMethodSecurityInterceptor;
import io.security.corespringsecurity.security.factory.MethodResourceMapFactoryBean;
import io.security.corespringsecurity.security.processor.ProtectPointcutPostProcessor;
import io.security.corespringsecurity.security.service.SecurityResourceService;

/**
 * Method 방식 - 주요 아키텍처
 *
 * <pre>
 *      - 인가 처리를 위한 초기화 과정과 진행
 *          - 초기화 과정
 *              1. 초기화 시 전체 Bean 을 검사하면서 보안이 설정된 Method 가 있는지 탐색
 *              2. Bean 의 Proxy 객체 생성
 *              3. 보안 Method 에 인가처리(권한심사) 기능을 하는 Advice 등록
 *              4. Bean 참조시 실제 Bean 이 아닌 Proxy Bean 객체를 참조
 *
 *          - 진행 과정
 *              1. Method 호출 시 Proxy 객체를 통해 Method 호출
 *              2. Advice 가 등록되어 있다면 Advice 를 작동하게 하여 인가 처리
 *              3. 권한 심사 통과하면 실제 Bean 의 Method 를 호출한다.
 * </pre>
 * <p>
 * <p>
 * Method 방식 - Map 기반 DB 연동
 *
 * <pre>
 *      - Annotation 설정 방식이 아닌 Map 기반으로 권한 설정
 *      - 기본적인 구현이 완성되어 있고, DB 로부터 자원과 권한정보를 맵핑함
 *      - 데이터를 전달하면 Method 방식의 인가처리가 이루어지는 클래스
 *
 *      - MethodResourcesMapFactoryBean
 *          - DB 로부터 얻은 권한 / 자원 정보를 ResourceMap 을 Bean 으로 생성해서 MapBasedMethodSecurityMetadataSource 에 전달
 * </pre>
 * <p>
 * <p>
 * ProtectPointcutPostProcessor
 *
 * <pre>
 *      - Method 방식의 인가처리를 위한 자원 및 권한 정보 설정 시 자원에 포인트 컷 포현식을 사용할 수 있도록 지원하는 Class
 *      - 빈 후 처리기로서 Spring 초기화 과정에서 Bean 을 검사하여 Bean 이 가진 Method 중에서 포인트 컷 표현식과 Matching 되는 클래스, 메서드, 권한정보를 MapBasedMethodSecurityMetadataSource 에 전달하여 인가 처리가 되도록 제공하는 클래스
 *      - DB 저장 방식
 *          - Method 방식
 *              - io.security.service.OrderService.order: ROLE_USER
 *          - Pointcut 방식
 *              - execution(* io.security.service.*Service.*(..)): ROLE_USER
 *      - 설정 클래스에서 Bean 생성기 접근제한자가 Package 범위로 되어 있기 때문에 Reflection 을 이용해 생성한다.
 * </pre>
 */
@RequiredArgsConstructor
@EnableGlobalMethodSecurity
@Configuration
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final SecurityResourceService securityResourceService;

    @Override
    protected MethodSecurityMetadataSource customMethodSecurityMetadataSource() {
        return mapBasedMethodSecurityMetadataSource();
    }

    @Bean
    public MapBasedMethodSecurityMetadataSource mapBasedMethodSecurityMetadataSource() {
        return new MapBasedMethodSecurityMetadataSource(methodResourcesMap().getObject());
    }

    @Bean
    public MethodResourceMapFactoryBean pointcutResourcesMap() {
        return new MethodResourceMapFactoryBean(securityResourceService, "pointcut");
    }

    @Bean
    public MethodResourceMapFactoryBean methodResourcesMap() {
        return new MethodResourceMapFactoryBean(securityResourceService, "method");
    }

    /**
     * ! 일단, 안된다...
     * <p>
     * 강사님말로는, Aspect 쪽 처리부분에 lambda 부분 처리에 exception 처리가 안되있어서 라는데, 모르겠다.
     * <p>
     * * 그래서 강사님 코드 그냥 훔쳤다.
     */
//    @Bean
//    BeanPostProcessor protectPointcutPostProcessor() throws Exception {
//
//        Class<?> clazz = Class.forName(
//            "org.springframework.security.config.method.ProtectPointcutPostProcessor");
//
//        Constructor<?> declaredConstructor = clazz.getDeclaredConstructor(
//            MapBasedMethodSecurityMetadataSource.class);
//        declaredConstructor.setAccessible(true);
//
//        Object instance = declaredConstructor.newInstance(mapBasedMethodSecurityMetadataSource());
//
//        Method setPointcutMap = instance.getClass().getDeclaredMethod("setPointcutMap", Map.class);
//        setPointcutMap.setAccessible(true);
//        setPointcutMap.invoke(instance, pointcutResourcesMap().getObject());
//
//        return (BeanPostProcessor) instance;
//
//    }
    @Bean
    public ProtectPointcutPostProcessor protectPointcutPostProcessor() {
        ProtectPointcutPostProcessor protectPointcutPostProcessor =
            new ProtectPointcutPostProcessor(mapBasedMethodSecurityMetadataSource());

        protectPointcutPostProcessor.setPointcutMap(pointcutResourcesMap().getObject());

        return protectPointcutPostProcessor;
    }

    @Bean
    public CustomMethodSecurityInterceptor customMethodSecurityInterceptor(
        MapBasedMethodSecurityMetadataSource methodSecurityMetadataSource) {
        CustomMethodSecurityInterceptor customMethodSecurityInterceptor = new CustomMethodSecurityInterceptor();
        customMethodSecurityInterceptor.setAccessDecisionManager(accessDecisionManager());
        customMethodSecurityInterceptor.setAfterInvocationManager(afterInvocationManager());
        customMethodSecurityInterceptor.setSecurityMetadataSource(methodSecurityMetadataSource);

        RunAsManager runAsManager = runAsManager();

        if (runAsManager != null) {
            customMethodSecurityInterceptor.setRunAsManager(runAsManager);
        }

        return customMethodSecurityInterceptor;
    }

}
