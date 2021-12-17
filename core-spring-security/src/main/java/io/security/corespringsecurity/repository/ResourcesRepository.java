package io.security.corespringsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.security.corespringsecurity.domain.entity.Resources;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

}
