package io.github.myifeng.swan.auth.dao;

import io.github.myifeng.swan.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account,String> {

}
