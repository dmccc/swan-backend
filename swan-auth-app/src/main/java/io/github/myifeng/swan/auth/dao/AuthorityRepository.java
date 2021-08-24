package io.github.myifeng.swan.auth.dao;

import io.github.myifeng.swan.auth.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority,String> {

}
