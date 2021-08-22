package io.github.myifeng.swan.auth.dao;

import io.github.myifeng.swan.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {

}
