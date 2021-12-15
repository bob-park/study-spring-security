package io.security.corespringsecurity.repository;

import io.security.corespringsecurity.domain.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

}
