package io.github.myifeng.swan.starter.auth;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AutoConfigureOrder(1)
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class ResourceServerAutoConfiguration extends ResourceServerConfigurerAdapter implements WebMvcConfigurer {


	@Bean
	public AccountDetailsArgumentResolver accountDetailsArgumentResolver() {
		return new AccountDetailsArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(accountDetailsArgumentResolver());
	}


	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}

	@Bean
	protected JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
			@Override
			public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
				val authentication = super.extractAuthentication(map);
				val userAuthentication = (AbstractAuthenticationToken) authentication.getUserAuthentication();
				val roles = userAuthentication.getAuthorities().stream()
						.filter(a -> a.getAuthority().startsWith("ROLE_"))
						.map(GrantedAuthority::getAuthority)
						.map(r -> r.substring(5))
						.collect(Collectors.toSet());
				val authorities = userAuthentication.getAuthorities().stream()
						.filter(a -> !a.getAuthority().startsWith("ROLE_"))
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());
				userAuthentication.setDetails(AccountDetails.of(map.get("user_name").toString(), null, roles, authorities));
				return authentication;
			}
		};
		converter.setSigningKey("swan");
		converter.setVerifierKey("swan");
		return converter;
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint())
				.and()
				.authorizeRequests()
				.anyRequest().permitAll();
	}

}
