package io.security.corespringsecurity.service.role.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.security.corespringsecurity.domain.entity.RoleHierarchy;
import io.security.corespringsecurity.repository.RoleHierarchyRepository;
import io.security.corespringsecurity.service.role.RoleHierarchyService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoleHierarchyServiceImpl implements RoleHierarchyService {

    private final RoleHierarchyRepository roleHierarchyRepository;

    @Override
    public String findAllHierarchy() {

        List<RoleHierarchy> roleHierarchies = roleHierarchyRepository.findAll();

        StringBuilder concatRoles = new StringBuilder();

        for (RoleHierarchy roleHierarchy : roleHierarchies) {

            if (roleHierarchy.getParentName() != null) {
                concatRoles
                    .append(roleHierarchy.getParentName().getChildName())
                    .append(" > ")
                    .append(roleHierarchy.getChildName())
                    .append("\n");
            }

        }

        return concatRoles.toString();
    }
}
