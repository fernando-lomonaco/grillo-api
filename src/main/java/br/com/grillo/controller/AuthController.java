package br.com.grillo.controller;

import br.com.grillo.dto.AuthDTO;
import br.com.grillo.dto.resource.AuthModelAssembler;
import br.com.grillo.dto.response.Response;
import br.com.grillo.dto.security.JwtResponseDTO;
import br.com.grillo.dto.security.LoginDTO;
import br.com.grillo.service.UserService;
import br.com.grillo.util.security.JwtTokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Tag(name = "API Auth", description = "Routes of auth")
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthModelAssembler assembler;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService service;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("signin")
    public ResponseEntity<Response<JwtResponseDTO>> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        Response<JwtResponseDTO> response = new Response<>();
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userDetails = userDetailsService.loadUserByUsername(loginDTO.getUsername());
        String jwt = jwtTokenUtil.getToken(userDetails);

        response.setData(JwtResponseDTO.create(jwt, userDetails.getUsername()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("signup")
    public ResponseEntity<Response<AuthDTO>> registerUser(@Valid @RequestBody AuthDTO authDTO) {
        Response<AuthDTO> response = new Response<>();

        Boolean existsUsername = service.existsByUsername(authDTO.getUsername());
        Boolean existsEmail = service.existsByEmail(authDTO.getEmail());

        if (Boolean.TRUE.equals(existsUsername)) {
            response.addErrorMsgToResponse("Aviso: Usuario ja existe.");
            return ResponseEntity.badRequest().body(response);
        } else if (Boolean.TRUE.equals(existsEmail)) {
            response.addErrorMsgToResponse("Aviso: E-mail ja esta em uso.");
            return ResponseEntity.badRequest().body(response);
        }

        final var auth = authDTO.convertDTOToEntity();
        AuthDTO model = assembler.toModel(service.save(auth));
        response.setData(model);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}