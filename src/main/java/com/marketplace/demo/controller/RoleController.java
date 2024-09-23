package com.marketplace.demo.controller;

import com.marketplace.demo.controller.converter.DTOConverter;
import com.marketplace.demo.controller.dto.RoleDTO;
import com.marketplace.demo.domain.Role;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.service.RoleService.RoleService;
import com.marketplace.demo.service.UserService.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/role", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class RoleController {

    private RoleService roleService;
    private DTOConverter<RoleDTO, Role> dtoConverter;

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        List<RoleDTO> roleDTOS = new ArrayList<>();

        roleService.readAll().forEach(r -> roleDTOS.add(dtoConverter.toDTO(r)));
        return roleDTOS;
    }

    @GetMapping(path = "/{id}")
    public RoleDTO getRoleById(@PathVariable("id") Long id) {
        return dtoConverter.toDTO(roleService.readById(id).get());
    }

    @PostMapping
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO) {
        return dtoConverter.toDTO(roleService.create(dtoConverter.toEntity(roleDTO)));
    }

    @GetMapping(path = "/{name}")
    public RoleDTO getRoleByName(@PathVariable("name") String name) {
        return dtoConverter.toDTO(roleService.findByName(name).get());
    }

    @DeleteMapping(path = "/{id}")
    public void deleteRole(@PathVariable("id") Long id) {
        roleService.deleteById(id);
    }
}