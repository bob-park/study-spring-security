package io.security.corespringsecurity.security.configure;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import io.security.corespringsecurity.security.factory.UrlResourcesMapFactoryBean;
import io.security.corespringsecurity.security.filer.PermitAllFilter;
import io.security.corespringsecurity.security.handler.CustomAccessDeniedHandler;
import io.security.corespringsecurity.security.handler.CustomAuthenticationFailureHandler;
import io.security.corespringsecurity.security.handler.CustomAuthenticationSuccessHandler;
import io.security.corespringsecurity.security.metadatasource.UrlFilterInvocationSecurityMetadataSource;
import io.security.corespringsecurity.security.provider.CustomAuthenticationProvider;
import io.security.corespringsecurity.security.service.SecurityResourceService;
import io.security.corespringsecurity.security.voter.IpAddressVoter;

@RequiredArgsConstructor
@Order(1)
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    private final SecurityResourceService securityResourceService;

//    private final AuthenticationSuccessHandler successHandler;
//    private final AuthenticationFailureHandler failureHandler;

    private final String[] permitAllResources = {"/", "/login", "/user/login/**"};

    /**
     * WebIgnore 설정
     *
     * <pre>
     *     - js / css / image 파일 등 보안 필터를 적용할 필요가 없는 리소스를 설정
     * </pre>
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // * and() 메서드를 통해 chaining 이 가능하지만, 가독성을 위해 기능별로 분리하여 작성했다.
        http
            .authorizeRequests()
//            .antMatchers("/", "/users", "user/login/**", "/login*").permitAll()
//            .antMatchers("/mypage").hasRole("USER")
//            .antMatchers("/messages").hasRole("MANAGER")
//            .antMatchers("/config").hasRole("ADMIN")
            .anyRequest().authenticated();

        http
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login_proc")
            .authenticationDetailsSource(authenticationDetailsSource)
            .defaultSuccessUrl("/")
            .successHandler(customAuthenticationSuccessHandler())
            .failureHandler(customAuthenticationFailureHandler())
            .permitAll();

        http
            // 기본 Filter 보다 먼저 확인하도록 한다.
            // ! 하지만, FilterSecurityInterceptor 는 한번만 적용된다.
            // ! 가장 마지막에 존재하는 FilterSecurityInterceptor 보다 앞에 위치한 customFilterSecurityInterceptor 가 먼저 실행 되면, 그 요청은 다음 필터인 FilterSecurityInterceptor 를 실행하지 않고, 바로 다음 filter 로 넘겨버린다.
            // ! http.antMatcher() 는 Spring Security 에서 기본적으로 생성하는 SecurityMetadataSource 를 통해 FilterSecurityInterceptor 에서 인가처리를 진행한다.
            // ! 따라서, Custom 한 FilterSecurityInterceptor 를 설정할 경우, 위에 설정한 http.antMatcher() 는 더 이상 동작하지 않는다.
            .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler());

    }

    /**
     * PasswordEncoder
     *
     * <pre>
     *      - 비밀번호를 안전하게 암호화 하도록 제공
     *      - Spring Security 5.0 이전에는 기본 PasswordEncoder 가 평문을 지원하는 NoOpPasswordEncoder(현재는 Deprecated)
     *
     *      - 생성
     *          - PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
     *          - 여러개의 PasswordEncoder 유형을 선언한 뒤, 상황에 맞게 선택해서 사용할 수 있도록 지원하는 Encoder 이다.
     *
     *      - 암호화 포맷 : {id}encodedPassword
     *          - 기본 포맷은 Bcrypt : {bcrypt}....
     *          - 알고리즘 종류 : bcrypt, noop, pbkdf2, scrypt, sha256
     *
     *      - Interface
     *          - encode(password)
     *              - password 암호화
     *          - matches(rawPassword, encodedPassword)
     *              - password 비교
     * </pre>
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler("/denied");
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public PermitAllFilter customFilterSecurityInterceptor() throws Exception {
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources);

        permitAllFilter.setSecurityMetadataSource(
            urlFilterInvocationSecurityMetadataSource());
        permitAllFilter.setAccessDecisionManager(affirmativeBased());
        permitAllFilter.setAuthenticationManager(authenticationManagerBean());

        return permitAllFilter;
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        return new UrlFilterInvocationSecurityMetadataSource(
            urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    @Bean
    public AccessDecisionManager affirmativeBased() {

        return new AffirmativeBased(getAccessDecisionVoters());
    }

    @Bean
    public List<AccessDecisionVoter<?>> getAccessDecisionVoters() {

        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();

        accessDecisionVoters.add(ipAddressVoter());
        accessDecisionVoters.add(roleVoter());

        return accessDecisionVoters;
    }

    @Bean
    public UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        return new UrlResourcesMapFactoryBean(securityResourceService);
    }

    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter() {
        return new RoleHierarchyVoter(roleHierarchy());
    }

    @Bean
    public IpAddressVoter ipAddressVoter() {
        return new IpAddressVoter(securityResourceService);
    }

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        return new RoleHierarchyImpl();
    }
}
