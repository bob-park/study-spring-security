package io.security.corespringsecurity.security.init;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

import io.security.corespringsecurity.service.role.RoleHierarchyService;

/**
 * URL 방식 - 권한 계층 적용하기
 *
 * <pre>
 *      ! Spring Security 에서 기본적으로 ROLE_USER, ROLE_ADMIN 등 다른 권한으로 취급 (문자열 차이)
 *
 *      - RoleHierarchy
 *          - 상위 계층 Role 은 하위 계층 Role 의 자원에 접근 가능함
 *          - ROLE_ADMIN > ROLE_MANAGER > ROLE_USER 일 경우 ROLE_ADMIN 만 있으면 하위 ROLE 의 권한을 모두 포함한다.
 *
 *      - RoleHierarchyVoter
 *          - RoleHierarchy 를 생성자로 받으며 이 클래스에서 설정한 규칙이 적용되어 심사함
 * </pre>
 */
@RequiredArgsConstructor
@Component
public class SecurityInitializer implements ApplicationRunner {

    private final RoleHierarchyService roleHierarchyService;

    private final RoleHierarchyImpl roleHierarchy;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String roleHierarchies = roleHierarchyService.findAllHierarchy();

        roleHierarchy.setHierarchy(roleHierarchies);
    }
}
