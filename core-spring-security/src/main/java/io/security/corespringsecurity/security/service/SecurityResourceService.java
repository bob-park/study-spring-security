package io.security.corespringsecurity.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import io.security.corespringsecurity.repository.ResourcesRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SecurityResourceService {

    private final ResourcesRepository resourcesRepository;

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> getResourceList() {

        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> result = new LinkedHashMap<>();

        resourcesRepository.findAllResources().forEach(resources -> {
            List<ConfigAttribute> configAttributeList = new ArrayList<>();

            resources.getRoleSet()
                .forEach(role -> configAttributeList.add(new SecurityConfig(role.getRoleName())));

            result.put(new AntPathRequestMatcher(resources.getResourceName()),
                configAttributeList);

        });

        return result;

    }

}