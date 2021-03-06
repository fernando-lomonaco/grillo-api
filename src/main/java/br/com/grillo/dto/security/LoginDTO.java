package br.com.grillo.dto.security;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

	@NotBlank(message = "Nome do usuário é obrigatório")
	private String username;
	@NotBlank(message = "Senha é obrigatória")
	private String password;

}