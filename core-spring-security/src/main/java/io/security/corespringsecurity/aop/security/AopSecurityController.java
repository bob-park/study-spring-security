package io.security.corespringsecurity.aop.security;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import io.security.corespringsecurity.domain.dto.AccountDto;

/**
 * Method 방식 - 개요
 *
 * <pre>
 *
 *     ! URL 방식은 Filter 기반
 *
 *     ! Method 방식은 AOP 기반
 *
 *      - 서비스 계층의 인가 처리 방식
 *          - 화면, 메뉴 단위가 아닌 기능 단위로 인가처리
 *          - 메소드 처리 전, 후로 보안 검사 수행하여 인가 처리
 *
 *      - AOP 기반으로 동작
 *          - Proxy 와 Advice 로 method 인가 처리 수행
 *
 *      - 보안 설정 방식
 *          - Annotation 권한 설정 방식
 *              - @PreAuthorize("hasRole('USER')")
 *              - @PostAuthorize("hasRole('USER')")
 *              - @Secured("hasRole('USER')")
 *
 *          - Map 기반 권한 설정 방식
 *              - Map 기반 방식으로 외부와 연동하여 Method 보안 설정 구현
 *
 * </pre>
 * <p>
 * <p>
 * Annotation 권한 설정
 *
 * <pre>
 *      - 보안이 필요한 Method 에 설정한다.
 *      - @PreAuthorized, @PostAuthorize
 *          - SpEL 지원
 *          - @PreAuthorized("hasRole('USER')) and (#account.username == principal.username)")
 *          - PrePostAnnotationSecurityMetadataSource 가 담당
 *
 *      - @Secured("ROLE_USER"), @RolesAllowed
 *          - SpEL 미지원
 *          - @Secured("ROLE_USER"), @RolesAllowed("ROLE_USER)
 *          - SecuredAnnotationSecurityMetadataSource, Jsr250MethodSecurityMetadataSource 가 담당
 *
 *      - @EnableGlobalMethodSecurity(prePostEnable = true, securedEnabled = true)
 *          - 반드시, Configuration 에 선언해야 동작한다.
 *
 * </pre>
 */
@RequiredArgsConstructor
@Controller
public class AopSecurityController {

    private final AopMethodService aopMethodService;
    private final AopPointcutService aopPointcutService;

    /**
     * ! 반드시, Method 보안 설정을 사용하려면, Configuration 에 @EnableGlobalMethodSecurity 를 선언해야한다.
     *
     * @param accountDto
     * @param model
     * @param principal
     * @return
     */
    @GetMapping(path = "preAuthorize")
    @PreAuthorize("hasRole('USER') and #accountDto.username == principal.username")
    public String preAuthorize(AccountDto accountDto, Model model, Principal principal) {

        model.addAttribute("method", "Success @PreAuthorize.");

        return "aop/method";
    }

    @GetMapping("/methodSecured")
    public String methodSecured(Model model) {
        aopMethodService.methodSecured();

        model.addAttribute("method", "Success MethodSecured");

        return "aop/method";
    }

    @GetMapping(path = "pointcutSecured")
    public String pointcutSecured(Model model) {

        aopPointcutService.notSecured();
        aopPointcutService.pointcutSecured();

        model.addAttribute("method", "Success PointcutSecured");

        return "aop/method";

    }

}
