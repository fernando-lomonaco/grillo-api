package br.com.grillo.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JwtResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private String username;

}