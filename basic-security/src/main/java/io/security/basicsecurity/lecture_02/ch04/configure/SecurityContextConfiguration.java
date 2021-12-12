package io.security.basicsecurity.lecture_02.ch04.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * SecurityContext
 *
 * <pre>
 *      - SecurityContext
 *          - Authentication 객체가 저장되는 보관소로 필요시 언제든지 Authentication 객체를 꺼내어 쓸 수 있도록 제공하는 클래스
 *          - ThreadLocal 에 저장되어 아무곳에서나 참조가 가능하도록 설계함
 *              - ThreadLocal 에 대한 자세한 사항은 `Spring 고급편` 에서 참고하자.
 *          - 인증이 완료되면 HttpSession 에 저장되어 Application 전반에 걸쳐 전역적인 참조가 가능하다.
 *
 *      - SecurityContextHolder
 *          - SecurityContext 객체 저장 방식
 *              - MODE_THREADLOCAL : Thread 당 SecurityContext 객체를 할당, default
 *              - MODE_INHERITABLETHREADLOCAL : Main Thread 와 Child Thread 에 관하여 동일한 SecurityContext 를 유지
 *              - MODE_GLOBAL : 응용 프로그램에서 단 하나의 SecurityContext 를 저장한다.
 *          - SecurityContextHolder.clearContext()
 *              - SecurityContext 기존 정보 초기화
 *
 *
 *      - Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
 *          - Controller 에서 파라미터에 @AuthenticationPrincipal 쓰면 바로 사용가능한 줄 알았는데, 안되네...쩝...
 *              - @AuthenticationPrincipal 은 Spring Security 의 UserDetail 을 구현한 클래스를 주입받는 것이였음
 *              - 따라서, Authentication.getPrincipal() 이였다.
 * </pre>
 */
@EnableWebSecurity
@Configuration
public class SecurityContextConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin();

        // SecurityContext 모드 바꾸기
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }
}
