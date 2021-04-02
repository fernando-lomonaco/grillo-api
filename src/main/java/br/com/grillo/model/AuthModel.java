package br.com.grillo.model;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class AuthModel extends RepresentationModel<AuthModel> {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotNull(message = "Deve-se atribuir uma senha")
    @Size(min = 6, max = 40)
    private String password;
    @NotNull(message = "Deve-se atribuir um perfil")
    @Size(min = 1, max = 2)
    private Set<String> role;

}