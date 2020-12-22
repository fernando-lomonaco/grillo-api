package br.com.grillo.config.jwt;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {

	private String token;
	@Builder.Default
	private String type = "Bearer";
	private UUID code;
	private String username;
	private String email;
	@Setter(value = AccessLevel.NONE)
	private List<String> roles;

	// public JwtResponse(String accessToken, UUID code, String username, String email, List<String> roles) {
	// 	this.token = accessToken;
	// 	this.code = code;
	// 	this.username = username;
	// 	this.email = email;
	// 	this.roles = roles;
	// }

}