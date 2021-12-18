package io.security.corespringsecurity.security.metadatasource;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.security.corespringsecurity.security.service.SecurityResourceService;

/**
 * Spring Security 의 인가 처리
 *
 * <pre>
 *       - http.antMatcher("user").access("hasRole('USER')")
 *          - 인증 정보
 *              - 사용자
 *          - 요청 정보
 *              - 자원
 *          - 권한 정보
 *              - 권한
 *  </pre>
 * <p>
 * <p>
 * SecurityMetadataSource
 *
 * <pre>
 *      - 자원에 설정된 권한정보를 추출하도록 구현
 *
 *      - FilterInvocationSecurityMetadataSource
 *          - Url 권한 정보 추출
 *      - MethodSecurityMetadataSource
 *          - Method 권한 정보 추출
 * </pre>
 * <p>
 * <p>
 * FilterInvocationSecurityMetadataSource
 *
 * <pre>
 *       - 사용자가 접근하고자 하는 URL 작원에 대한 정보를 추출
 *       - AccessDecisionManager 에게 전달하여 인가 처리 수행
 *       - DB 로부터 자원 및 권한 정보를 맵핑하여 Map 으로 관리
 *       - 사용자의 매 요청마다 요청 정보에 맵핑된 권한 정보 확인
 * </pre>
 */
@RequiredArgsConstructor
public class UrlFilterInvocationSecurityMetadataSource implements
    FilterInvocationSecurityMetadataSource {

    private final Map<RequestMatcher, List<ConfigAttribute>> requestMap;
    private final SecurityResourceService securityResourceService;

    // 파라미터에 FilterInvocation 가 온다.
    // 하지만, 다른 SecurityMetadataSource 에서도 사용해야하기 떄문에, Type 이 Object 인것
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object)
        throws IllegalArgumentException {

        HttpServletRequest request = ((FilterInvocation) object).getRequest();

        // url 추출
        for (Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {

            RequestMatcher matcher = entry.getKey();

            if (matcher.matches(request)) {
                return entry.getValue(); // return 권한 정보
            }

        }

        // ! null 또는 emptyList 를 반환하면 권한을 체크하지 않고 통과시켜버린다.
        return Collections.emptyList();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        Set<ConfigAttribute> allAttributes = new HashSet<>();

        for (Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    public void reload() {
        requestMap.clear();
        requestMap.putAll(securityResourceService.getResourceList());
    }
}
