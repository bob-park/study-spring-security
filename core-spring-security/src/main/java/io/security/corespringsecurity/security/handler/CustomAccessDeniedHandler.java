package io.security.corespringsecurity.security.handler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * 인증 예외는 해당하는 인증 예외 filter 가 받지만
 *
 * <p>
 * 인증 최족 확인하는 FilterSecurityInterceptor 에서 인가 확인 중 exception 을 throw 하고 ExceptionTransitionFilter 가
 * 받아서 accessDeniedHandler 를 호출한다.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final String errorPage;

    public CustomAccessDeniedHandler(String errorPage) {
        this.errorPage = errorPage;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String deniedUrl = errorPage + "?exception=" + URLEncoder.encode(accessDeniedException.getMessage(), StandardCharsets.UTF_8);

        response.sendRedirect(deniedUrl);
    }
}
