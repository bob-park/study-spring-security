package io.security.corespringsecurity.security.factory;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.security.corespringsecurity.security.service.SecurityResourceService;

/**
 * Url 방식 - Map 기반 DB 연동
 *
 *
 * <pre>
 *      - UrlResourcesMapFactoryBean
 *          - DB 로 부터 얻는 권한/자원 정보를 RequestMap 을 Bean 으로 생성해서 UrlFilterInvocationSecurityMetadataSource 에 전달
 * </pre>
 */
@RequiredArgsConstructor
public class UrlResourcesMapFactoryBean implements
    FactoryBean<Map<RequestMatcher, List<ConfigAttribute>>> {

    private final SecurityResourceService securityResourceService;
    private Map<RequestMatcher, List<ConfigAttribute>> resourceMap;

    @Override
    public Map<RequestMatcher, List<ConfigAttribute>> getObject() {

        if (resourceMap == null) {
            // 데이터가 없는 경우 DB 로 부터 가져오도록 설정
            init();
        }

        return resourceMap;
    }

    @Override
    public Class<?> getObjectType() {
        return Map.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void init() {
        resourceMap = securityResourceService.getResourceList();
    }
}
