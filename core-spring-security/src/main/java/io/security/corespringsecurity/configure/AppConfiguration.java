package io.security.corespringsecurity.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.security.corespringsecurity.repository.ResourcesRepository;
import io.security.corespringsecurity.security.service.SecurityResourceService;

@EnableJpaAuditing
@Configuration
public class AppConfiguration {

    @Bean
    public SecurityResourceService securityResourceService(
        ResourcesRepository resourcesRepository) {
        return new SecurityResourceService(resourcesRepository);
    }

}
