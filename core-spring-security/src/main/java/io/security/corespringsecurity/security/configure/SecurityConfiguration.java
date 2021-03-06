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
     * WebIgnore ??????
     *
     * <pre>
     *     - js / css / image ?????? ??? ?????? ????????? ????????? ????????? ?????? ???????????? ??????
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

        // * and() ???????????? ?????? chaining ??? ???????????????, ???????????? ?????? ???????????? ???????????? ????????????.
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
            // ?????? Filter ?????? ?????? ??????????????? ??????.
            // ! ?????????, FilterSecurityInterceptor ??? ????????? ????????????.
            // ! ?????? ???????????? ???????????? FilterSecurityInterceptor ?????? ?????? ????????? customFilterSecurityInterceptor ??? ?????? ?????? ??????, ??? ????????? ?????? ????????? FilterSecurityInterceptor ??? ???????????? ??????, ?????? ?????? filter ??? ???????????????.
            // ! http.antMatcher() ??? Spring Security ?????? ??????????????? ???????????? SecurityMetadataSource ??? ?????? FilterSecurityInterceptor ?????? ??????????????? ????????????.
            // ! ?????????, Custom ??? FilterSecurityInterceptor ??? ????????? ??????, ?????? ????????? http.antMatcher() ??? ??? ?????? ???????????? ?????????.
            .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class);

        http
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler());

    }

    /**
     * PasswordEncoder
     *
     * <pre>
     *      - ??????????????? ???????????? ????????? ????????? ??????
     *      - Spring Security 5.0 ???????????? ?????? PasswordEncoder ??? ????????? ???????????? NoOpPasswordEncoder(????????? Deprecated)
     *
     *      - ??????
     *          - PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
     *          - ???????????? PasswordEncoder ????????? ????????? ???, ????????? ?????? ???????????? ????????? ??? ????????? ???????????? Encoder ??????.
     *
     *      - ????????? ?????? : {id}encodedPassword
     *          - ?????? ????????? Bcrypt : {bcrypt}....
     *          - ???????????? ?????? : bcrypt, noop, pbkdf2, scrypt, sha256
     *
     *      - Interface
     *          - encode(password)
     *              - password ?????????
     *          - matches(rawPassword, encodedPassword)
     *              - password ??????
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
