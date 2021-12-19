package io.security.corespringsecurity.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.security.corespringsecurity.domain.dto.AccountDto;
import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.repository.RoleRepository;
import io.security.corespringsecurity.repository.UserRepository;
import io.security.corespringsecurity.service.user.UserService;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Account> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public AccountDto getUser(Long id) {

        ModelMapper mapper = new ModelMapper();

        Account account = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Not found."));

        List<String> roles = account.getUserRoles()
            .stream()
            .map(Role::getRoleName)
            .collect(Collectors.toList());

        AccountDto accountDto = mapper.map(account, AccountDto.class);

        accountDto.setRoles(roles);

        return accountDto;
    }

    @Transactional
    @Override
    public void createUser(Account account) {
        Role role = roleRepository.findByRoleName("ROLE_USER");

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        account.setUserRoles(roles);

        userRepository.save(account);
    }

    @Transactional
    @Override
    public void modifyUser(AccountDto accountDto) {

        Account account = userRepository.findById(accountDto.getId())
            .orElseThrow(() -> new RuntimeException("Not found."));

        if (accountDto.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            accountDto.getRoles().forEach(role -> {
                Role r = roleRepository.findByRoleName(role);
                roles.add(r);
            });
            account.setUserRoles(roles);
        }

        account.setUsername(accountDto.getUsername());
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setEmail(accountDto.getEmail());
        account.setAge(accountDto.getAge());

    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Secured("ROLE_MANAGER")
    public void order() {

        log.info("order");

    }
}
