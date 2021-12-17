package io.security.corespringsecurity.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.domain.entity.Role;
import io.security.corespringsecurity.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
// ! Spring Security 가 기본적으로 생성한 UserDetailsService 가 있기 때문에, Bean 의 이름을 명시한다.
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("not found username."));

        List<GrantedAuthority> collect = account.getUserRoles()
            .stream()
            .map(Role::getRoleName)
            .collect(Collectors.toSet())
            .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new AccountContext(account, collect);
    }
}
