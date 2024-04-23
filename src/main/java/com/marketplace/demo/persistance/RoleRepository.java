package com.marketplace.demo.persistance;

import com.marketplace.demo.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
