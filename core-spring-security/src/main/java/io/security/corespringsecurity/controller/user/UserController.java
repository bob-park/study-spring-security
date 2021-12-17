package io.security.corespringsecurity.controller.user;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import io.security.corespringsecurity.domain.dto.AccountDto;
import io.security.corespringsecurity.domain.entity.Account;
import io.security.corespringsecurity.service.user.UserService;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    @GetMapping(value = "/mypage")
    public String myPage() throws Exception {

        return "user/mypage";
    }

    @GetMapping(path = "users")
    public String createUser() {
        return "user/login/register";
    }

    @PostMapping(path = "users")
    public String createUser(AccountDto accountDto) {

        Account account = Account.builder()
            .username(accountDto.getUsername())
            .email(accountDto.getEmail())
            .password(passwordEncoder.encode(accountDto.getPassword()))
            .age(accountDto.getAge())
            .build();

        userService.createUser(account);

        return "redirect:/";
    }
}
