package io.security.basicsecurity.lecture_02.ch02.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 필터 초기화와 다중 설정 클래스
 *
 * <pre>
 *   - 설정 클래스 별로 보안 기능이 각각 작동
 *   - 설정 클래스 별로 RequestMatcher 설정
 *      - http.antMatcher("/admin/**")
 *   - 설정 클래스 별로 필터가 생성
 *   - FilterChainProxy 가 각 필터를 가지고 있음
 *   - 요청에 따라 RequestMatcher 와 매칭되는 필터가 작동하도록 함
 *
 *
 *   ! 설정 클래스가 2개라면 FilterChainProxy 안 SecurityFilterChains List 에 각 SecurityFilterChain 에 추가된다.
 *      - 설정 클래스가 다중으로 있어도, RequestMatcher 만 다르면 해당하는 요청의 인증 / 인가를 처리한다.
 *      - 필터 선택(?)
 *          1. 요청 정보와 match 된 SecurityFilterChain 선택 후
 *          2. 인증 / 인가 처리
*
 *      ! 따라서, RequestMatcher 는 넓은 범위가 순서가 더 뒤에 있어야 한다.
 *          - 넓은 범위가 앞에 있는 경우 좁은 범위의 RequestMatcher 는 확인하지 않는다.
 *
 * </pre>
 */
@Slf4j
//@Configuration
//@EnableWebSecurity
@Order(0)
// ! WebSecurityConfigurers 가 초기화 될때 순서를 지정하지 않을 경우 예외가 발생한다.
// Order 의 순서에 따라 SecurityFilterChains List 에 순차적으로 add 된다.
public class Ch02Configuration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/admin/**")
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }

}

@Order(1)
//@Configuration
class SecurityConfiguration2 extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .formLogin();
    }
}


