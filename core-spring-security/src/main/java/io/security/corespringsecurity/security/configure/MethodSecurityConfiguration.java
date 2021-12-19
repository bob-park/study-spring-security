package io.security.corespringsecurity.security.configure;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.method.MapBasedMethodSecurityMetadataSource;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import io.security.corespringsecurity.security.factory.MethodResourceMapFactoryBean;
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
    public MethodResourceMapFactoryBean methodResourcesMap() {
        return new MethodResourceMapFactoryBean(securityResourceService);
    }
}
