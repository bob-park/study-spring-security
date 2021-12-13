package io.security.basicsecurity.lecture_02.ch01.configure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * * 위임 필터 및 필터 빈 초기화
 * <pre>
 *   ! Spring Security 는 Filter 를 기반으로 인증 / 인가 를 확인 및 동작한다.
 *
 *   - DelegatingFilterProxy
 *      - Servlet Filter 는 Spring 에서 정의된 빈을 주입해서 사용할 수 없음
 *      - 특정한 이름을 가진 Spring Bean 을 찾아 그 bean 에게 요청을 위임
 *          - springSecurityFilterChain 이름으로 생성된 Bean 을 ApplicationContext 에서 찾아 요청을 위
 *          - 실제 보안처리를 하지 않음
 *
 *
 *  - FilterChainProxy
 *      - springSecurityFilterChain 의 이름으로 생성되는 Filter Bean
 *      - DelegatingFilterProxy 으로 부터 요청을 위임받고 실제 보안처리
 *      - Spring Security 초기화 시 생성되는 필터들을 관리하고 제어
 *          - Spring Security 가 기본적으로 생성하는 필터
 *          - 설정 클래스에서 API 추가 시 생성되는 필터
 *      - 사용자의 요청을 필터 순서대로 호출하여 전달
 *      - 사용자 정의 필터를 생성해서 기존의 필터 전, 후로 추가 가능
 *          - 필터의 순서를 잘 정의
 *      - 마지막 필터까지 인증 및 인가 예외가 발생하지 않으면 보안 통과
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
//@EnableWebSecurity
//@Configuration
public class Ch01Configuration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    }
}
