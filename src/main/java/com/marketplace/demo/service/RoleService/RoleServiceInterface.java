package com.marketplace.demo.service.RoleService;

import com.marketplace.demo.domain.Role;
import com.marketplace.demo.service.CrudService;

import java.util.Optional;

public interface RoleServiceInterface extends CrudService<Role, Long> {
    public Optional<Role> findByName(String Name);
}
