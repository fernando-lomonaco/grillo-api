package br.com.grillo.model.resource;

import br.com.grillo.controller.AuthController;
import br.com.grillo.model.AuthModel;
import br.com.grillo.model.entity.Auth;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthModelAssembler extends RepresentationModelAssemblerSupport<Auth, AuthModel> {

    public AuthModelAssembler() {
        super(AuthController.class, AuthModel.class);
    }

    @Override
    public AuthModel toModel(Auth auth) {
        AuthModel authModel = instantiateModel(auth);
        authModel.setUsername(auth.getUsername());
        authModel.setEmail(auth.getEmail());
        authModel.setRole(auth.getRoles().stream().map(c -> c.getName().name()).collect(Collectors.toSet()));

        return authModel;
    }
}
