package com.marketplace.demo.controller.converter;

import com.marketplace.demo.controller.dto.UserDTO;
import com.marketplace.demo.domain.User;
import com.marketplace.demo.persistance.PaymentRepository;
import com.marketplace.demo.persistance.PostRepository;
import com.marketplace.demo.persistance.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDTOConverter implements DTOConverter<UserDTO, User> {

    private final PostRepository postRepository;
    private final PaymentRepository paymentRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDTO toDTO(User user) {
        return null;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        return null;
    }
}
