package io.security.basicsecurity.controller;

import java.security.Principal;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Security 의 의존성 추가 시 일어나는 일
 *
 * <pre>
 *     - Server 가 기동되면 Spring Security 의 초기화 작업 및 보안 설정이 이루어진다.
 *     - 별도의 설정을 구현하지 않아도 기본적인 웹 보안 기능이 현재 시스템에 연동되어 작동함
 *          - 모든 요청은 인증이 되어야 자원에 접근이 가능하다
 *          - 인증 방식은 Form Login 방식과 Http Basic 로그인 방식을 제공한다.
 *          - 기본 로그인 페이지가 제공된다.
 *          - 기본 계정 한 개가 제공된다
 *              - username : user
 *              - password : Random Characters
 *
 *      - 문제점
 *          - 계정 추가, 권한 추가, DB 연동 등
 *          - 기본적인 보안 기능 외에 시스템에서 필요로 하는 더 세부적이고 추가적인 보안 기능이 필요
 * </pre>
 */
@Slf4j
@RestController
public class SecurityController {

    @GetMapping(path = "/")
    public String index(HttpSession session, @AuthenticationPrincipal User user,
        Authentication auth1) {

        Authentication auth2 = SecurityContextHolder.getContext().getAuthentication();

        SecurityContext context = (SecurityContext) session.getAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        Authentication auth3 = context.getAuthentication();

        return "home";

    }

    @GetMapping("thread")
    public String thread() {

        new Thread(() -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("authentication={}", authentication);

        }).start();

        return "thread";
    }

    /**
     * 단순히 로그인 페이지에 문자열만 출력하도록 한다.
     *
     * @return
     */
    @GetMapping(path = "loginPage")
    public String loginPage() {
        return "loginPage";
    }

}
