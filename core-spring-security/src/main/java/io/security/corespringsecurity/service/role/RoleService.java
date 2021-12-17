package io.security.corespringsecurity.service.role;

import java.util.List;

import io.security.corespringsecurity.domain.entity.Role;

public interface RoleService {

    Role getRole(long id);

    List<Role> getRoles();

    void createRole(Role role);

    void deleteRole(long id);

}
