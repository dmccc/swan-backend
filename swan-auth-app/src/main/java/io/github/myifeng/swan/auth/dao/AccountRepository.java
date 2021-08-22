package io.github.myifeng.swan.auth.dao;

import io.github.myifeng.swan.auth.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,String> {

	@Query("select a from Account a left join fetch a.authorities left join fetch a.roles where a.username=:val or a.mobile=:val or a.id=:val")
	<T> Optional<T> findByUsernameOrMobileOrId(@Param("val") String usernameOrMobileOrId, Class<T> accountType);

}
