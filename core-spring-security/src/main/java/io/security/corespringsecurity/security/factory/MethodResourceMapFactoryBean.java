package io.security.corespringsecurity.security.factory;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.access.ConfigAttribute;

import io.security.corespringsecurity.security.service.SecurityResourceService;

@RequiredArgsConstructor
public class MethodResourceMapFactoryBean implements
    FactoryBean<Map<String, List<ConfigAttribute>>> {

    private final SecurityResourceService securityResourceService;
    private final String resourceType;

    private Map<String, List<ConfigAttribute>> resourceMap;

    @Override
    public Map<String, List<ConfigAttribute>> getObject() {
        if (resourceMap == null) {
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

        if ("method".equals(resourceType)) {
            resourceMap = securityResourceService.getMethodResourceList();
        } else if ("pointcut".equals(resourceType)) {
            resourceMap = securityResourceService.getPointcutResourceList();
        }

    }
}
