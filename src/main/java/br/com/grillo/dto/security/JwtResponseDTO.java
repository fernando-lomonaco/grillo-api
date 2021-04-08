package br.com.grillo.dto.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class JwtResponseDTO {

    private final String token;
    private final String type;
    private final String username;

    public static JwtResponseDTO create(String token, String username) {
        return new JwtResponseDTO(token, "Bearer", username);
    }

}