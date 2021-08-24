package io.github.myifeng.swan.auth.service;

import io.github.myifeng.swan.auth.dao.AccountRepository;
import io.github.myifeng.swan.auth.entity.Account;
import io.github.myifeng.swan.auth.entity.Authority;
import io.github.myifeng.swan.auth.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;


@Service
public class SwanUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private AccountRepository accountRepo;

	@Override
	public UserDetails loadUserByUsername(String usernameOrId) throws UsernameNotFoundException {
		Account account = accountRepo.findByUsernameOrMobileOrId(usernameOrId,Account.class)
				.orElseThrow(() -> new UsernameNotFoundException("invalid username or id:" + usernameOrId));
		return User.builder()
				.username(account.getId())
				.password(account.getPassword())
				.authorities(
						Stream.concat(
								account.getRoles().stream()
										.map(Role::getName)
										.map(n -> "ROLE_" + n),
								account.getAuthorities().stream()
										.map(Authority::getName)
						).toArray(String[]::new)
				)
				.build();
	}

}