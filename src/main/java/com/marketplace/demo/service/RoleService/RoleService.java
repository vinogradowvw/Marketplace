package com.marketplace.demo.service.RoleService;

import com.marketplace.demo.domain.Role;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.RoleRepository;
import com.marketplace.demo.persistance.UserRepository;
import com.marketplace.demo.service.CrudServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class RoleService extends CrudServiceImpl<Role, Long> implements RoleServiceInterface {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Override
    public void deleteById(Long id) throws IllegalArgumentException {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("Role with id " + id + " does not exist");
        }

        Role role = roleRepository.findById(id).get();

        for (User user : role.getUsers()){
            user.getRoles().remove(role);
            userRepository.save(user);
        }

        roleRepository.deleteById(id);
    }

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    protected CrudRepository<Role, Long> getRepository() {
        return roleRepository;
    }
}
