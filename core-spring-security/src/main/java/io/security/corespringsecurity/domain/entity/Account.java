package io.security.corespringsecurity.domain.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;
    private String email;
    private Integer age;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @JoinTable(name = "account_roles", joinColumns = {
        @JoinColumn(name = "account_id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id")})
    private Set<Role> userRoles = new HashSet<>();

    @Builder
    protected Account(Long id, String username, String password, String email, Integer age,
        Set<Role> userRoles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;

        this.userRoles = userRoles;
    }
}
