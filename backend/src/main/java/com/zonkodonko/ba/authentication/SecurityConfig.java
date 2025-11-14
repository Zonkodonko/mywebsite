package com.zonkodonko.ba.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Config for Authorization
 *
 * @author Z0nko
 * @version 14.08.2025
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
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
				.requestMatchers(HttpMethod.GET, "/blog/**/articles").permitAll()
				.anyRequest().authenticated());
		http.oauth2ResourceServer((oauth2) -> oauth2.opaqueToken(Customizer.withDefaults()));
		return http.build();
	}
}

