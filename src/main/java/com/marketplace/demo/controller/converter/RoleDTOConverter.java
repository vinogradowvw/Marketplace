package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.RoleDTO;
import com.marketplace.demo.domain.Role;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class RoleDTOConverter implements DTOConverter<RoleDTO, Role> {

    private final UserService userService;

    @Override
    public RoleDTO toDTO(Role role) {
        List<Long> users = new ArrayList<>();

        Optional<List<User>> optUsers = Optional.ofNullable(role.getUsers());
        if (optUsers.isPresent()) {
            for (User user : optUsers.get()) {
                users.add(user.getID());
            }
        }

        return new RoleDTO(role.getID(), role.getName(), users);
    }

    @Override
    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.id());
        role.setName(roleDTO.name());

        List<User> users = new ArrayList<>();

        Optional<List<Long>> optUsers = Optional.ofNullable(roleDTO.users());
        if (optUsers.isPresent()) {
            for (Long id : optUsers.get()) {
                users.add(userService.readById(id).get());
            }
        }

        role.setUsers(users);
        return role;
    }
}
