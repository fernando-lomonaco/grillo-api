package br.com.grillo.dto.resource;

import br.com.grillo.controller.AuthController;
import br.com.grillo.dto.AuthDTO;
import br.com.grillo.dto.CategoryDTO;
import br.com.grillo.model.Auth;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class AuthModelAssembler extends RepresentationModelAssemblerSupport<Auth, AuthDTO> {

    public AuthModelAssembler() {
        super(AuthController.class, AuthDTO.class);
    }

    @Override
    public AuthDTO toModel(Auth auth) {
        //AuthDTO authDTO = instantiateModel(auth);
        return AuthDTO.builder()
                .username(auth.getUsername())
                .password(auth.getPassword())
                .email(auth.getEmail())
                .role(auth.getRole().getValue())
                .build();
    }
}
