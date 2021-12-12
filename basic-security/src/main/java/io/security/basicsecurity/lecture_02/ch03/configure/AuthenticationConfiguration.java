package io.security.basicsecurity.lecture_02.ch03.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *
 * Authentication
 *
 * <pre>
 *      - 당신이 누구인지 증명하는 것
 *          - 사용자의 인증 정보를 저장하는 토큰 개념
 *          - 인증시 id 와 password 를 담고 인증 검증을 위해 전달되어 사용된다.
 *          - 인증 후 최종 인증 결과 (user 객체, 권한정보) 를 담고, SecurityContext 에 저장되어 전역적으로 참조가 가능하다.
 *              - Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
 *
 *          - 구조
 *              1) principal : 사용자 id 혹은 User 객체를 저장
 *              2) credentials : 사용자 password
 *              3) authorities : 인증된 사용자의 권한 목록
 *              4) details : 인증 부가 정보
 *              5) Authenticated : 인증 여부
 * </pre>
 *
 */
//@EnableWebSecurity
//@Configuration
public class AuthenticationConfiguration extends WebSecurityConfigurerAdapter {

}
