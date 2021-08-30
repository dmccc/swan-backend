package io.github.myifeng.swan.auth.dao;

import io.github.myifeng.swan.auth.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client,String> {

    Optional<Client> findByClientId(String clientId);
}
