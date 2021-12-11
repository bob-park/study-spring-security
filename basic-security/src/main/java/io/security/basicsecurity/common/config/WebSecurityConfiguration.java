package io.security.basicsecurity.common.config;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

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
//        http
//            .authorizeRequests()
//            .anyRequest().authenticated();  // 어떠한 요청에서도 인증을 요구한다.

        /*
         * Form Login
         */
//        http
//            .formLogin() // 인증을 요구할 때, Form Login 으로 한다.
////            .loginPage("/loginPage") // 사용자 정의 Login Page - 이 경로는 접근할 때 인증에서 제외한다.
//            .defaultSuccessUrl("/") // login 성공 후 이동 페이지
//            .failureUrl("/login?error=true") // 로그인 실패 후 이동 페이지
//            .usernameParameter("userId") // 아이디 파라미터명 설정
//            .passwordParameter("passwd") // password parameter 설정
//            .loginProcessingUrl("/login_proc") // 로그인 form action url
////            .successHandler((request, response, authentication) -> {
////                log.info("authentication={}", authentication.getName());
////
////                response.sendRedirect("/");
////            }) // 로그인 성공 후 핸들러
////            .failureHandler((request, response, e) -> {
////                log.info("exception={}", e.getMessage());
////
////                response.sendRedirect("/loginPage");
////            }) // 로그인 실패 후 핸들러
//            .permitAll() // 이 login page 는 인증을 받지 않도록 설정한다.
//        ;

        /*
         인증 API - Logout
            - 세션 무효화, 인증토큰 삭제, 쿠키정보 삭제, 로그인 페이지로 redirect
            - default 로 항상 post 방식으로 진행해야 한다.
         */
//        http
//            .logout() // Logout 기능이 작동함
//            .logoutUrl("/logout") // 로그아웃 처리 URL
//            .logoutSuccessUrl("/loginPage") // 로그아웃 성공 후 이동 페이지
//            .deleteCookies("remember") // 로그아웃 후 쿠키 삭제
//            .addLogoutHandler((request, response, authentication) -> {
//                HttpSession session = request.getSession();
//
//                session.invalidate();
//            }) // 로그아웃 핸들러 - 쿠키 삭제 외 추가 작업이 필요한 경우 정의
//            .logoutSuccessHandler((request, response, authentication) -> {
//                // Redirect 뿐 아니라 다양한 로직도 실행할 수 있음
//
//                response.sendRedirect("/loginPage");
//            }) // 로그아웃 후 핸들러
//        ;

        /*
         인증 API - Remember Me 인증

         - 세션이 만료되고 Web Browser 가 종료된 후에도 Application 이 사용자를 기억하는 기능
         - Remember-Me 쿠키에 대한 Http 요청을 확인한 후 Token 기반 인증을 사용해 유효성을 검사하고 Token 이 검증되면 사용자는 로그인 된다.

         - 사용자 라이프 사이클
            - 인증 성공(Remember-Me 쿠키 설정)
            - 인증 실패(쿠키가 존재하면 쿠키 무효화)
            - 로그아웃 (쿠키가 존재하면 쿠키 무효화)
         */
//        http
//            .rememberMe() // rememberMe 기능 작동함
//            .rememberMeParameter("remember") // 기본 파라미터를 해당 문자열로 변경, default : remember-me
//            .tokenValiditySeconds(3_600) // 만료 시간 설정
////            .alwaysRemember(true) // Remember Me 기능이 활성화되지 않아도 항상 실행, default : false
//            // remember me 기능에서 사용자 계정을 조회하여 처리할 때 사용하는 service
//            .userDetailsService(userDetailsService)
//        ;

        /*
         인증 API - 익명 사용자 인증 필터

         AnonymousAuthenticationFilter

         - 익명 사용자 인증 처리 필터
         - 익명 사용자와 인증 사용자를 구분해서 처리하기 위한 용도로 사용
         - 화면에서 인증 여부를 구현할 때 isAnonymous() 와 isAuthenticated() 로 구분해서 사용
         - 인증 객체를 세션에 저장하지 않는다.
         */



        /*
         인증 API - 동시 세션 제어

         - 최대 세션 허용 개수 초과 시 (strategy 2개)
            - 이전 사용자 세션 만료
            - 현재 사용자 인증 실패
                - 추후 로그인 한 계정 인증 예외 발생
         */
//        http
//            .sessionManagement() // 세션 관리 기능이 작동함
//            .maximumSessions(1) // 최대 혀용 가능 세션 수, -1 입력시 무제한 로그인 세션 허용
//            .maxSessionsPreventsLogin(false) // 동시 로그인 차단함, default : false (기본 로그인 세션 만료)
////            .invalidSessionUrl("/invalid") // 세션이 유효하지 않을 때 이동할 페이지 - 없는데?
//            .expiredUrl("/expired") // 세션이 만료된 경우 이동할 페이지
//        ;

        /*
         인증 API - 세션 고정 보호

         - 공격자 접속
            - 사용자에게 공격자 세션 쿠키 사용

         - 사용자 접속
            - 공격자 세션쿠키로 로그인 시도

         - 사용자가 공격자 세션쿠키로 로그인 성공할 경우 공격자도 인증된 것처럼 세션을 사용할 수 있음
            - 공격자 쿠키 값으로 인증되어 있기 때문에 공격자는 사용자 정보를 공유


         ! 하지만, Spring Security 는 사용자가 세션쿠키로 로그인할 경우 새로운 세션 ID 을 발급하여 넘겨주기 때문에, 세션을 보호할 수 있다.

         인증 API - 세션 고정 보호 전략
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
         인증 API - 세션 정책

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

        /*
         인증 API - 세션 제어 필터

         SessionManagementFilter

         - 세션 관리
            - 인증시 사용자의 session 정보를 등록, 조회, 삭제 등의 session 이력을 관리
         - 동시적 세션 제어
            - 동일 계정으로 접속이 허용되는 최대 session 수 제한
         - 세션 고정 보호
            - 인증할 때마다 session cookie 를 새로 발급하여 공격자의 cookie 조작 방지
         - 세션 생성 정책
            - ALWAYS, IF_REQUIRED, NEVER, STATELESS



         ConcurrentSessionFilter

         - SessionManagementFilter 와 서로 연계해서 동작함
         - 매 요청마다 현재 사용자의 세션만료 여부 체크
         - 세션이 만료되었을 경우 즉시 만료 처리
            - session.isExpired() == true
                - 로그아웃 처리
                - 즉시 오류 페이지 응답 ("This session has been expired.")

         */

        /*
         인가 API - 권한 설정

         - 선언적 방식
            - URL
                - http.antMatcher("/user/**").hasRole("USER")
            - Method
                @PreAuthorize("hasRole('USER')")
                public void void user(){...}

         - 동적 방식 - DB 연동 프로그래밍
            - URL
            - Method
         */
        /*
         인가 API - 표현식

         - authenticated() : 인증된 사용자의 접근 허용
         - fullyAuthenticated() : 인증된 사용자의 접근 허용, rememberMe 인증 제외
         - permitAll() : 무조건 접근 허용
         - denyAll() : 무조건 접근 허용하지 않음
         - anonymous() : 익명 사용자의 접근 허용 - 익명 사용자한테 기본적으로 ROLE_ANONYMOUS 가 부여되므로, 그외 ROLE_USER, ... 등은 접근하지 못한다.
         - rememberMe() : rememberMe 를 통해 인증된 사용자의 접근 허용
         - access(String) : 주어진 SpEL 표현식의 평가 결과가 true 이면 접근을 허용
         - hasRole(String) : 사용자가 주어진 역할이 있다면 접근 허용 - prefix 인 ROLE_ 를 제거한 문자열
         - hasAuthority(String) : 사용자가 주어진 권한이 있다면 접근 허용
         - hasAnyRole(String...) : 사용자에 주어진 역할 중 어떤 것이라도 있다면 접근 허용 - prefix 인 ROLE_ 를 제거한 문자열
         - hasAnyAuthority(String...) : 사용자가 주어진 권한 중 어떤 것이라도 있다면 접근 허용
         - hasIpAddress(String...) : 주어진 IP 로부터 요청이 왔다면 접근 허용
         */
        // ! 주의사항 - 설정 시 구체적인 경로가 먼저 오고 그것 보다 큰 범위의 경로가 뒤에 오도록 해야한다.
//        http
//            .antMatcher("/shop/**") // 설정한 경로에 보안기능이 작동한다. - 생략시 모든 경로에 대해서 보안기능이 작동한다.
//            .authorizeRequests()
//            // 설정한 URL Pattern 에 일치할 경우 뒤에 설정한 권한을 확인한다.
//            .antMatchers("/shop/login", "/shop/user/**").permitAll()
//            .antMatchers("/shop/mypage").hasRole("USER")
//            // access 를 사용하면 표현식을 사용해서 다양한 권한을 확인할 수 있다.
//            .antMatchers("/shop/admin/pay").access("hasRole('ADMIN')")
//            .antMatchers("/shop/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
//            .anyRequest().authenticated() // 그외 요청은 인증을 받은 사용자만 접근 가능
//        ;

        http
            .authorizeRequests()
            .antMatchers("/login").permitAll()
            .antMatchers("/user").hasRole("USER")
            .antMatchers("/admin/pay").hasRole("ADMIN")
            .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')")
            .anyRequest().authenticated()
        ;

//        http
//            .formLogin();


        /*
         인증/인가 API

         ExceptionTranslationFilter

         - AuthenticationException
            - 인증 예외 처리
                1. AuthenticationEntryPoint 호출
                    - 로그인 페이지 이동, 401 오류 코드 전달 등
                    - implement 가능

                2. 인증 예외가 발생하기 전의 요청 정보를 저장
                    - RequestCache : 사용자의 이전 요청 정보를 Session 에 정하고 이를 꺼내 오는 Cache 메커니즘
                        - SavedRequest : 사용자가 요청했던 request 파라미터 값들, 그 당시의 헤더값들 등이 저장

         - AccessDeniedException
            - 인가 예외 처리
                - AccessDeniedHandler 에서 예외 처리하도록 제공
         */
        http
            .exceptionHandling() // 예외처리 기능이 작동함
//            .authenticationEntryPoint((request, response, authException) -> {
//                // Spring Security 가 제공하는 login 페이지가 아닌 구현한 Controller 로 이동된다.
//                // 따라서, 구현되어 있어야 정상 동작 한다.
//                response.sendRedirect("/login");
//            }) // 인증 실패 시 처리
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.sendRedirect("/denied");
            }) // 인증 실패 시 처리
        ;

        http
            .formLogin()
            .successHandler((request, response, authentication) -> {
                // 인증 실패 시 RequestCache 에 사용자가 가고자 하는 url 및 요청 파라미터 정보 등을 저장하고 있어, 로그인 성공시 바로 이동시킬 수 있다.
                RequestCache requestCache = new HttpSessionRequestCache();

                SavedRequest savedRequest = requestCache.getRequest(request, response);

                String redirectUrl = savedRequest.getRedirectUrl();

                response.sendRedirect(redirectUrl);
            })
        ;

        /*
          Form 인증 - CSRF (사이트 간 요청 위조)

          1. 사용자가 로그인 후 쿠키를 발급받음
          2. 공격자가 링크를 이용자에게 전달
          3. 사용자가 링크를 클릭하여 공격용 웹페이지에 접속
          4. 사용자가 공격용 페이지를 열면, 브라우저는 이미지 파일을 받아오기 위해 공격용 URL 을 연다.
          5. 사용자의 승인이나 인지 벗이 배송지가 등록됨으로써 공격이 완료된다.

          From 인증 - CsrfFilter

          - 모든 요청에 랜덤하게 생성된 토큰을 HTTP 파라미터로 요구
          - 요청 시 전달되는 토큰 값과 서버에 저장된 실제 값과 비교한 후 만약 일치하지 않으면 요청은 실패한다.
         */
        http
            .csrf() // 기본 활성화되어 있음
            .disable() // 비활성화
            ;
    }

    /**
     * 사용자 생성 builder method
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        // In Memory 방식
        /*
         password 앞에 prefix 가 존재할 경우 특정한 알고리즘의 의한 암호화를 뜻함
         ! 만일 지정하지 않을 경우, id null 이라는 exception 이 발생함

         - noop : 암호화를 별도로 지정하지 않겠다는 의미
         */
        auth.inMemoryAuthentication()
            .withUser("user").password("{noop}1234").roles("USER")
            .and()
            .withUser("sys").password("{noop}1234").roles("SYS", "USER")
            .and()
            .withUser("admin").password("{noop}1234").roles("ADMIN", "SYS", "USER")
        // 추후 권한 계층을 주어 처리할 것
        ;

    }
}
