package io.security.basicsecurity.lecture_02.ch09.configure;

/**
 * Authorization
 *
 * <pre>
 *      - 당신에게 무엇이 허가 되었는지 증명하는 것
 *
 *      - Spring Security 가 지원하는 권한 계층
 *
 *          - 웹 계층
 *              - URL 요청에 따른 메뉴 혹은 화면 단위의 레벨 보안
 *
 *          - 서비스 계층
 *              - 화면 단위가 아닌 메서드 같은 기능 단위의 레벨 보안
 *
 *
 *          - 도메인 계층(Access Control List, 접근제어목록)
 *              - 객체 단위의 레벨 보안
 *              - 이번 강좌에서는 다루지 않음
 *
 * </pre>
 *
 *
 * <p>
 * FilterSecurityInterceptor
 *
 * <pre>
 *      - 인가 처리를 담당하는 필터
 *      - 가장 맨 마지막에 위치함
 *      - 최종적으로 사용자가 접근하고자 하는 자원에 대한 승인 / 거부를 결정함
 *
 *      - 마지막에 위치한 필터로써 인증된 사용자에 대하여 특정 요청의 승인 / 거부 여부를 최종적으로 결정
 *      - 인증객체 없이 보호자원에 접근을 시도할 경우 AuthenticationException 발생
 *      - 인증 후 자원에 접근 가능한 권한이 존재하지 않을 경우 AccessDeniedException 을 발생
 *      - 권한 제어 방식 중 Http 자원의 보안을 처리하는 필터
 *      - 권한 처리를 AccessDecisionManager 에게 맡김
 * </pre>
 */
public class AuthorizationAndFilterConfiguration {

}
