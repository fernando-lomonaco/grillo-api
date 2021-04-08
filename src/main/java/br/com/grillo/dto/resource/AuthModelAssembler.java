package br.com.grillo.dto.resource;

import br.com.grillo.controller.AuthController;
import br.com.grillo.dto.AuthDTO;
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
        AuthDTO authDTO = instantiateModel(auth);
        authDTO.setUsername(auth.getUsername());
        authDTO.setPassword(auth.getPassword());
        authDTO.setEmail(auth.getEmail());
        authDTO.setRole(auth.getRole().getValue());
        return authDTO;
    }
}
