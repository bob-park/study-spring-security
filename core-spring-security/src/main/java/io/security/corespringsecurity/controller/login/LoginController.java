package io.security.corespringsecurity.controller.login;

import io.security.corespringsecurity.domain.entity.Account;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping(path = {"login", "api/login"})
    public String login(@RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "exception", required = false) String exception, Model model) {

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "user/login/login";
    }

    @GetMapping(path = "logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/";
    }

    @GetMapping(path = {"denied", "api/denied"})
    public String accessDenied(
        @RequestParam(value = "exception", required = false) String exception, Model model,
        Authentication authentication) {

        Account account = (Account) authentication.getPrincipal();

        model.addAttribute("username", account.getUsername());
        model.addAttribute("exception", exception);

        return "user/login/denied";
    }
}
