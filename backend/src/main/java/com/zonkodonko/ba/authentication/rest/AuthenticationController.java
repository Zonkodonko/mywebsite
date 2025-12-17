package com.zonkodonko.ba.authentication.rest;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * todo write comment
 *
 * @author Z0nko
 * @version 14.08.2025
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	private final RestClient restClient;
	private final String clientId;
	private final String clientSecret;

	Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	// Beispiel: http://localhost:8080/realms/zonkodonko/protocol/openid-connect
	public AuthenticationController(
			@Value("${spring.security.oidc.base-url}") String oidcBaseUrl,
			@Value("${spring.security.oidc.client-id}") String clientId,
			@Value("${spring.security.oidc.client-secret}") String clientSecret
	) {
		this.restClient = RestClient.builder()
				.baseUrl(oidcBaseUrl)
				.build();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	// POST /api/auth/token mit JSON: { "username": "...", "password": "...", "scope": "openid profile email" }
	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> token(@Valid @RequestBody LoginRequest body) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "password");
		form.add("client_id", clientId);
		form.add("client_secret", clientSecret);
		form.add("username", body.username());
		form.add("password", body.password());
		logger.info("login request: {}", body);

		try {
			TokenResponse tokens = restClient.post()
					.uri("/token")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.accept(MediaType.APPLICATION_JSON)
					.body(form)
					.retrieve()
					.body(TokenResponse.class);

			return ResponseEntity.ok(tokens);
		} catch (HttpClientErrorException e) {
			String errorBody = e.getResponseBodyAsString();
			logger.warn("Keycloak token request failed: status={}, body={}", e.getStatusCode(), errorBody);
			return ResponseEntity.status(e.getStatusCode()).body(errorBody);
		} catch (Exception e) {
			logger.error("Unexpected error during token request", e);
			return ResponseEntity.internalServerError().body("{\"error\":\"server_error\"}");
		}
	}

	@PostMapping(path = "/refresh", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> refreshToken(@RequestHeader Map<String, String> headers) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "refresh_token");
		form.add("client_id", clientId);
		form.add("client_secret", clientSecret);
		form.add("refresh_token", headers.get("refresh_token"));
		logger.info("refresh request: {}", headers);
		TokenResponse body = restClient.post()
				.uri("/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON)
				.body(form)
				.retrieve()
				.body(TokenResponse.class);
		return ResponseEntity.ok(body);
	}

	@PostMapping(path = "/logout")
	public ResponseEntity<?> logout(@RequestHeader Map<String, String> headers) {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("client_id", clientId);
		form.add("client_secret", clientSecret);
		form.add("refresh_token", headers.get("refresh_token"));
		ResponseEntity<Void> entity = restClient.post()
				.uri("/logout")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.APPLICATION_JSON)
				.body(form)
				.retrieve()
				.toBodilessEntity();
		if(entity.getStatusCode().isError()) {
			throw new RuntimeException("Error during logout");
		} else {
			return ResponseEntity.ok().build();
		}

	}

	public record LoginRequest(String username, String password) {
	}

	public record TokenResponse(
			String access_token,
			Long expires_in,
			Long refresh_expires_in,
			String refresh_token,
			String token_type,
			String id_token,
			String session_state,
			String scope
	) {}



}
