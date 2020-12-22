package br.com.grillo.model;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginModel {

	@NotBlank(message = "Nome do usuário é obrigatório")
	private String username;
	@NotBlank(message = "Senha é obrigatória")
	private String password;

}