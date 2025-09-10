package com.zonkodonko.ba.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;

/**
 * todo write comment
 *
 * @author Z0nko
 * @version 14.08.2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static ClientRegistrationRepository clientRegistrationRepository;

	private String oidcBaseUrl;
	private String clientId;
	private String clientSecret;

	public SecurityConfig(
			@Value("${spring.security.oidc.base-url}") String oidcBaseUrl,
			@Value("${spring.security.oidc.client-id}") String clientId,
			@Value("${spring.security.oidc.client-secret}") String clientSecret) {
		this.oidcBaseUrl = oidcBaseUrl;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults());
		http.csrf(csrf ->
			csrf.ignoringRequestMatchers("/auth/**", "/resume/**")
		);
        http.authorizeHttpRequests((authorize) -> authorize
		        .requestMatchers(HttpMethod.OPTIONS, "/auth/**").permitAll()
		        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
		        .requestMatchers(HttpMethod.GET, "/resume/**").permitAll()
		        .anyRequest().authenticated());
		http.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
		http.oauth2Login(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		if(clientRegistrationRepository == null) {
			clientRegistrationRepository = new InMemoryClientRegistrationRepository(keycloakClientRegistration());
		}
		return clientRegistrationRepository;

	}

	private ClientRegistration keycloakClientRegistration () {
		return ClientRegistration.withRegistrationId("keycloak")
				.clientId(this.clientId)
				.clientSecret(this.clientSecret)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
				.authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
//				.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
				.scope( "username", "role")
				.authorizationUri("http://localhost:8080/realms/zonkodonko/protocol/openid-connect/auth")
				.tokenUri("http://localhost:8080/realms/zonkodonko/protocol/openid-connect/token")
				.userInfoUri("http://localhost:8080/realms/zonkodonko/protocol/openid-connect/userinfo")
				.userNameAttributeName(IdTokenClaimNames.ACR)
				.jwkSetUri("http://localhost:8080/realms/zonkodonko/protocol/openid-connect/certs")
				.clientName("keycloak")
				.build();
	}
}
