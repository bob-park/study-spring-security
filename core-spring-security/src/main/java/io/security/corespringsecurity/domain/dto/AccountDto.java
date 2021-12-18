package io.security.corespringsecurity.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountDto {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Integer age;
    private List<String> roles;

}
