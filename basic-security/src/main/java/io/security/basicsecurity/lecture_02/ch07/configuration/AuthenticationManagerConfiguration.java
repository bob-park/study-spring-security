package io.security.basicsecurity.lecture_02.ch07.configuration;

/**
 * AuthenticationManager
 *
 * <pre>
 *      - AuthenticationProvider 목록 중에서 인증 처리 요건에 맞는 AuthenticationProvider 를 찾아 인증 처리를 위임한다.
 *      - 부모 ProviderManager 를 설정하여 AuthenticationProvider 를 계속 탐색 할 수 있다.
 *
 *      ! 실제 인증처리를 하지 않음
 *          - AuthenticationProvider 를 찾아 인증 처리를 위임만 한다.
 *
 *
 * </pre>
 */
public class AuthenticationManagerConfiguration {

}
