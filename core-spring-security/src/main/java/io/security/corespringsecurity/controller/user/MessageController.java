package io.security.corespringsecurity.controller.user;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {

    @GetMapping(value = "/messages")
    public String mypage() throws Exception {

        return "user/messages";
    }

    @ResponseBody
    @PostMapping(path = "api/messages")
    public Map<String, Object> apiMessage() {

        return Collections.singletonMap("data", "ok");
    }
}
