package io.security.corespringsecurity.security.metadatasource;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.log.LogMessage;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
public class UrlFilterInvocationSecurityMetadataSource implements
    FilterInvocationSecurityMetadataSource {

    private final Map<RequestMatcher, List<ConfigAttribute>> requestMap = new LinkedHashMap<>();

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
}
