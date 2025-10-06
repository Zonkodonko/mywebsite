package com.zonkodonko.ba.authentication.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

	private String hostDomain;
	private boolean isDev;

	public CorsConfiguration(@Value("${HOST_DOMAIN:localhost}") String hostDomain, @Value("${spring.application.dev:false}") boolean isDev) {
		this.hostDomain = hostDomain;
		this.isDev = isDev;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
		List<String> allowedOrigins = new ArrayList<>(List.of(
				"https://" + hostDomain,
				"https://*." + hostDomain
		));
		if(this.isDev) {
			allowedOrigins.add("http://localhost:4200");
		}
		config.setAllowedOriginPatterns(allowedOrigins);

		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of(
				"Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Cache-Control", "Pragma","refresh_token"
		));
		config.setAllowCredentials(true); // nur, wenn du Cookies/Authorization mit sendest
		config.setExposedHeaders(List.of("Location", "Content-Disposition"));
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
