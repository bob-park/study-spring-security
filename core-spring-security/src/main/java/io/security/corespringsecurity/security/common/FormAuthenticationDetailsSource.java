package io.security.corespringsecurity.security.common;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * Form 인증 - AuthenticationDetailsSource
 *
 * <p>
 * AuthenticationDetailsSource
 * <pre>
 *      - WebAuthenticationDetails 객체를 생성
 * </pre>
 */
@Component
public class FormAuthenticationDetailsSource implements
    AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new FormWebAuthenticationDetails(context);
    }
}
