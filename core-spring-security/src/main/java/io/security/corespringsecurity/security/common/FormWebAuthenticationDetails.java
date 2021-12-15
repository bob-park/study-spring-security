package io.security.corespringsecurity.security.common;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Form 인증 - WebAuthenticationDetails
 *
 * <p>
 * WebAuthenticationDetails
 *
 * <pre>
 *      - 인증 과정 중 전달된 데이터를 저장
 *      - Authentication 의 details 속성에 저장
 * </pre>
 */
public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    private final String secretKey;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);

        this.secretKey = request.getParameter("secret_key");
    }

    public String getSecretKey() {
        return secretKey;
    }
}
