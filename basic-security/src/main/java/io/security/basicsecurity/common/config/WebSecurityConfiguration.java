package io.security.basicsecurity.common.config;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 사용자 정의 보안 기능 구현
 *
 * <pre>
 *      - WebSecurityConfigurerAdapter class
 *          - Spring Security 의 Web 보안 기능 초기화 및 설정
 *      - HttpSecurity class
 *          - 세부적인 보안 기능을 설정할 수 있는 API 제공
 * </pre>
 *
 *
 * <p>
 * Form Login
 *
 * <pre>
 *      - 인증 받지 않는 사용자인 경우 Form Login 페이지로 이동한다.
 *      - 기본적으로 Spring Security 가 제공해주고 있으며, Custom 할 수 있다.
 *      - Session 및 인증 Token 생성 / 저장한다.
 *      - 세션에 저장된 인증 토큰으로 접근 / 인증 유지
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity // ! 반드시 선언해주어야 웹 보안이 설정된다.
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
         * 인증
         */
        http
            .authorizeRequests()
            .anyRequest().authenticated();  // 어떠한 요청에서도 인증을 요구한다.

        /*
         * Form Login
         */
        http
            .formLogin() // 인증을 요구할 때, Form Login 으로 한다.
//            .loginPage("/loginPage") // 사용자 정의 Login Page - 이 경로는 접근할 때 인증에서 제외한다.
            .defaultSuccessUrl("/") // login 성공 후 이동 페이지
            .failureUrl("/login?error=true") // 로그인 실패 후 이동 페이지
            .usernameParameter("userId") // 아이디 파라미터명 설정
            .passwordParameter("passwd") // password parameter 설정
            .loginProcessingUrl("/login_proc") // 로그인 form action url
//            .successHandler((request, response, authentication) -> {
//                log.info("authentication={}", authentication.getName());
//
//                response.sendRedirect("/");
//            }) // 로그인 성공 후 핸들러
//            .failureHandler((request, response, e) -> {
//                log.info("exception={}", e.getMessage());
//
//                response.sendRedirect("/loginPage");
//            }) // 로그인 실패 후 핸들러
            .permitAll() // 이 login page 는 인증을 받지 않도록 설정한다.
        ;

        /*
         Logout
            - 세션 무효화, 인증토큰 삭제, 쿠키정보 삭제, 로그인 페이지로 redirect
            - default 로 항상 post 방식으로 진행해야 한다.
         */
        http
            .logout() // Logout 기능이 작동함
            .logoutUrl("/logout") // 로그아웃 처리 URL
            .logoutSuccessUrl("/loginPage") // 로그아웃 성공 후 이동 페이지
            .deleteCookies("remember") // 로그아웃 후 쿠키 삭제
            .addLogoutHandler((request, response, authentication) -> {
                HttpSession session = request.getSession();

                session.invalidate();
            }) // 로그아웃 핸들러 - 쿠키 삭제 외 추가 작업이 필요한 경우 정의
            .logoutSuccessHandler((request, response, authentication) -> {
                // Redirect 뿐 아니라 다양한 로직도 실행할 수 있음

                response.sendRedirect("/loginPage");
            }) // 로그아웃 후 핸들러
        ;

        /*
         Remember Me 인증

         - 세션이 만료되고 Web Browser 가 종료된 후에도 Application 이 사용자를 기억하는 기능
         - Remember-Me 쿠키에 대한 Http 요청을 확인한 후 Token 기반 인증을 사용해 유효성을 검사하고 Token 이 검증되면 사용자는 로그인 된다.

         - 사용자 라이프 사이클
            - 인증 성공(Remember-Me 쿠키 설정)
            - 인증 실패(쿠키가 존재하면 쿠키 무효화)
            - 로그아웃 (쿠키가 존재하면 쿠키 무효화)
         */
        http
            .rememberMe() // rememberMe 기능 작동함
            .rememberMeParameter("remember") // 기본 파라미터를 해당 문자열로 변경, default : remember-me
            .tokenValiditySeconds(3_600) // 만료 시간 설정
//            .alwaysRemember(true) // Remember Me 기능이 활성화되지 않아도 항상 실행, default : false
            // remember me 기능에서 사용자 계정을 조회하여 처리할 때 사용하는 service
            .userDetailsService(userDetailsService)
        ;

        /*
         AnonymousAuthenticationFilter

         - 익명 사용자 인증 처리 필터
         - 익명 사용자와 인증 사용자를 구분해서 처리하기 위한 용도로 사용
         - 화면에서 인증 여부를 구현할 때 isAnonymous() 와 isAuthenticated() 로 구분해서 사용
         - 인증 객체를 세션에 저장하지 않는다.
         */



        /*
         동시 세션 제어

         - 최대 세션 허용 개수 초과 시 (strategy 2개)
            - 이전 사용자 세션 만료
            - 현재 사용자 인증 실패
                - 추후 로그인 한 계정 인증 예외 발생
         */
        http
            .sessionManagement() // 세션 관리 기능이 작동함
            .maximumSessions(1) // 최대 혀용 가능 세션 수, -1 입력시 무제한 로그인 세션 허용
            .maxSessionsPreventsLogin(false) // 동시 로그인 차단함, default : false (기본 로그인 세션 만료)
//            .invalidSessionUrl("/invalid") // 세션이 유효하지 않을 때 이동할 페이지 - 없는데?
            .expiredUrl("/expired") // 세션이 만료된 경우 이동할 페이지
        ;

        /*
         세션 고정 보호

         - 공격자 접속
            - 사용자에게 공격자 세션 쿠키 사용

         - 사용자 접속
            - 공격자 세션쿠키로 로그인 시도

         - 사용자가 공격자 세션쿠키로 로그인 성공할 경우 공격자도 인증된 것처럼 세션을 사용할 수 있음
            - 공격자 쿠키 값으로 인증되어 있기 때문에 공격자는 사용자 정보를 공유


         ! 하지만, Spring Security 는 사용자가 세션쿠키로 로그인할 경우 새로운 세션 ID 을 발급하여 넘겨주기 때문에, 세션을 보호할 수 있다.

         세션 고정 보호 전략
         - none
            - session 이 새롭게 생성되지 않음

         - changeSessionId
            - default
            - servlet 3.1 이상에서 기본값
            - 인증 시 새로 sessionId 를 생성
            - 이전 session 의 모든 속성값을 그대로 사용할 수 있음

         - migrateSession
            - changeSessionId 와 비슷
            - servlet 3.1 이하에서 작동 및 기본값
            - 이전 session 의 모든 속성값을 그대로 사용할 수 있음

         - newSession
            - changeSessionId 와 비슷
            - 이전의 session 에서의 속성값을 다 초기화됨
         */
//        http
//            .sessionManagement()
//            .sessionFixation().changeSessionId() // default 가 changeSessionId - servlet 3.1 기본값
//        ;

        /*
         세션 정책

         - SessionCreationPolicy.ALWAYS : Spring Security 가 항상 세션을 생성
         - SessionCreationPolicy.IF_REQUIRED : Spring Security 가 필요 시 생성(기본값)
         - SessionCreationPolicy.NEVER : Spring Security 가 생성하지 않지만, 이미 존재하면 사용
         - SessionCreationPolicy.STATELESS : Spring Security 가 생성하지 않고 존재해도 사용하지 않음
            - session 을 전혀 사용하지 않을 경우
            - jwt 등
         */
//        http
//            .sessionManagement()
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//        ;
    }
}
