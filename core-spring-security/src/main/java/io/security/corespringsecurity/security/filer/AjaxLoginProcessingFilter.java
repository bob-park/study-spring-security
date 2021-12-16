package io.security.corespringsecurity.security.filer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.security.corespringsecurity.domain.dto.AccountDto;
import io.security.corespringsecurity.security.token.AjaxAuthenticationToken;

import static org.springframework.util.StringUtils.hasText;

/**
 * Ajax 인증
 *
 * <pre>
 *      - AbstractAuthenticationProcessingFilter 상속
 *          - 모든 인증 처리를 담당
 *      - 필터 작동 조건
 *          - AntPathRequestMatcher("/api/login") 로 요청정보와 매칭하고 요청 방식이 Ajax 이면 필터 작동
 *              - 그러면 별도의 인증 컨트롤러를 안만들어도 되는거네?
 *      - AjaxAuthenticationToken 생성하여 AuthenticationManager 에게 전달하여 인증 처리
 *      - Filter 추가
 *          - http.addFilterBefore(AjaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
 *          - form 로그인을 담당하는 필터 클래스 앞에서 처리
 * </pre>
 */
public class AjaxLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AjaxLoginProcessingFilter() {
        // 해당 pattern 과 일치하면 작동
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        if (!isAjax(request)) {
            throw new IllegalStateException("Authentication is not support.");
        }

        AccountDto accountDto = objectMapper.readValue(request.getReader(), AccountDto.class);

        if (!hasText(accountDto.getUsername())
            || !hasText(accountDto.getPassword())) {
            throw new IllegalArgumentException("Username or Password is empty.");
        }

        AjaxAuthenticationToken ajaxAuthenticationToken =
            new AjaxAuthenticationToken(accountDto.getUsername(), accountDto.getPassword());

        // authenticationManager 로 인증을 위임하지만, 실제 처리하는 부분은 authenticationProvider 에서 처리를 해주어야 한다.
        // ! 만일, 처리할 AuthenticationProvider 가 없는 경우 인증 예외가 발생한다.
        return getAuthenticationManager().authenticate(ajaxAuthenticationToken);
    }

    private boolean isAjax(HttpServletRequest request) {

        String header = request.getHeader("X-Requested-With");

        return "XMLHttpRequest".equals(header);

    }
}
