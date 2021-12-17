package io.security.corespringsecurity.service.role.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.repository.RoleRepository;
import io.security.corespringsecurity.service.role.RoleService;

@Slf4j

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRole(long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found."));
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void createRole(Role role) {

        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }

}
