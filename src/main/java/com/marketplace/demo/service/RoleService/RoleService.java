package com.marketplace.demo.service.RoleService;

import com.marketplace.demo.domain.Role;
import com.marketplace.demo.persistance.RoleRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class RoleService extends CrudServiceImpl<Role, Long> implements RoleServiceInterface {

    private RoleRepository roleRepository;

    @Override
    protected CrudRepository<Role, Long> getRepository() {
        return roleRepository;
    }
}
