package br.com.grillo.controller;

import br.com.grillo.exception.EntityNotFoundException;
import br.com.grillo.dto.AuthDTO;
import br.com.grillo.dto.resource.AuthModelAssembler;
import br.com.grillo.service.security.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static br.com.grillo.util.Constants.USER_NOT_FOUND;

@RestController
@Tag(name = "User", description = "User controller")
@RequestMapping("users")
public class UserController {

    private final UserDetailsServiceImpl service;
    private final AuthModelAssembler assembler;

    public UserController(UserDetailsServiceImpl service, AuthModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Get user authenticated")
    @GetMapping("me")
    public ResponseEntity<AuthDTO> getUser(Principal principal) {
        return service.findByUsername(principal.getName()).map(assembler::toModel).map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND + principal.getName()));
    }

}
