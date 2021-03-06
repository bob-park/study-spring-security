package io.security.basicsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin/pay")
    public String adminPay() {
        return "adminPay";
    }

    @GetMapping("/admin/**")
    public String admin() {
        return "admin";
    }

}
