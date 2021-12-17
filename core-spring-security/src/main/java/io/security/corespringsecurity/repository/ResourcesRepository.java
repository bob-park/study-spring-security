package io.security.corespringsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.security.corespringsecurity.domain.entity.Resources;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    Optional<Resources> findByResourceNameAndHttpMethod(String resourceName, String httpMethod);

}
