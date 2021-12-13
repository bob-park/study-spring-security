package io.security.basicsecurity.lecture_02.ch05.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * SecurityContextPersistenceFilter
 *
 * <pre>
 *      - SecurityContext 객체의 생성, 저장 조회
 *
 *      - 익명 사용자
 *          - 새로운 SecurityContext 객체를 생성하여 <b>SecurityContextHolder 에서 저장</b>
 *          - AnonymousAuthenticationFilter 에서 AnonymousAuthenticationToken 객체를 SecurityContext 에 저장
 *
 *      - 인증시
 *          - 새로운 SecurityContext 객체를 생성하여 <b>SecurityContextHolder 에서 저장</b>
 *          - UsernamePasswordAuthenticationFilter 에서 인증 성공 후 SecurityContext 에 UsernamePasswordAuthentication 객체를 SecurityContext 에 저장
 *          - 인증이 최종 완료되면 Session 에 SecurityContext 를 저장
 *              - SecurityContextPersistenceFilter 가 저장함
 *
 *      - 인증후
 *          - Session 에서 SecurityContext 꺼내어 <b>SecurityContextHolder 에서 저장</b>
 *          - SecurityContext 안에 Authentication 객체가 존재하면 계속 인증을 유지한다.
 *
 *      - 최종 응답 시 공통
 *          - SecurityContextHolder.clearContext(0
 * </pre>
 */
@EnableWebSecurity
//@Configuration
public class SecurityContextPersistenceFilterConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin();

    }
}
