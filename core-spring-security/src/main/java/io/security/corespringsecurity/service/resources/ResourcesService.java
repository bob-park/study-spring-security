package io.security.corespringsecurity.service.resources;

import java.util.List;

import io.security.corespringsecurity.domain.entity.Resources;

public interface ResourcesService {

    Resources getResources(long id);

    List<Resources> getResources();

    void createResources(Resources resources);

    void deleteResources(long id);
}
