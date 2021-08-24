package io.github.myifeng.swan.starter.auth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AccountDetails {
    private String id;
    private String username;
    private Set<String> roles;
    private Set<String> authorities;

    public static AccountDetails of(
            String id, String username,
            Set<String> roles,
            Set<String> authorities
    ) {
        return new AccountDetails(id, username, roles, authorities);
    }
}
