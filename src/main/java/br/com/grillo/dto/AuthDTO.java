package br.com.grillo.dto;

import br.com.grillo.model.Auth;
import br.com.grillo.util.BcryptUtil;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthDTO extends RepresentationModel<AuthDTO> {

    @Getter
    @NotNull(message = "Deve-se atribuir um login.")
    @Size(min = 3, max = 20)
    private String username;

    @Getter
    @Size(max = 50, message = "Email deve ter no máximo 50 caracteres.")
    @Email(message = "Email invalido.")
    private String email;

    @NotNull(message = "Deve-se atribuir uma senha.")
    @Size(min = 6, message = "Senha deve conter ao menos 6 caracteres.")
    private String password;

    @Getter
    @NotNull(message = "Perfil do usuário nao pode ser nulo.")
    @Pattern(regexp = "^(ROLE_ADMIN|ROLE_USER)$",
            message = "Somente os perfis ROLE_ADMIN ou ROLE_USER são aceitos.")
    private String role;

    public String getPassword() {
        return BcryptUtil.getHash(this.password);
    }

    public Auth convertDTOToEntity() {
        return new ModelMapper().map(this, Auth.class);
    }

}