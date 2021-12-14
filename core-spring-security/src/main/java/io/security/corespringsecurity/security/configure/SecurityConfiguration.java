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
            .antMatchers("/", "/users").permitAll()
            .antMatchers("/mypage").hasRole("USER")
            .antMatchers("/messages").hasRole("MANAGER")
            .antMatchers("/config").hasRole("ADMIN")
            .anyRequest().authenticated()
        ;

        http
            .formLogin();
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
        return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 평문 암호화
    }

}
