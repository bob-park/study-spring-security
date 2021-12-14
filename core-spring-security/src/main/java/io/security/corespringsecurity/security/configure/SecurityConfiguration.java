package io.security.corespringsecurity.security.configure;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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

        String password = passwordEncoder().encode("12345");

        // 추후 권한 계층으로 할것임
        auth.inMemoryAuthentication()
            .withUser("user").password(password).roles("USER")
            .and()
            .withUser("manager").password(password).roles("MANAGER", "USER")
            .and()
            .withUser("admin").password(password).roles("ADMIN", "MANAGER", "USER")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/mypage").hasRole("USER")
            .antMatchers("/messages").hasRole("MANAGER")
            .antMatchers("/config").hasRole("ADMIN")
            .anyRequest().authenticated()
        ;

        http
            .formLogin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 평문 암호화
    }

}
