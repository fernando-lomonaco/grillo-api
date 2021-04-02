package br.com.grillo.controller;

import br.com.grillo.config.jwt.JwtResponse;
import br.com.grillo.config.jwt.JwtUtils;
import br.com.grillo.config.jwt.UserDetailsImpl;
import br.com.grillo.model.AuthModel;
import br.com.grillo.model.LoginModel;
import br.com.grillo.model.entity.Auth;
import br.com.grillo.model.entity.Role;
import br.com.grillo.model.enums.RoleType;
import br.com.grillo.repository.RoleRepository;
import br.com.grillo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
public class AuthController {

    private static final String MESSAGE_ERROR = "Error: Role is not found.";

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginModel loginModel) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        JwtResponse jwtResponse = JwtResponse.builder()
                .token(jwt)
                .code(userDetails.getCode())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AuthModel authModel) {

        if (userRepository.existsByUsername(authModel.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        } else if (userRepository.existsByEmail(authModel.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        Auth auth = new Auth(
                authModel.getUsername(),
                authModel.getEmail(),
                encoder.encode(authModel.getPassword())
        );

        Set<String> strRoles = authModel.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(MESSAGE_ERROR));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(MESSAGE_ERROR));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException(MESSAGE_ERROR));
                        roles.add(userRole);
                }
            });
        }

        auth.setRoles(roles);
        userRepository.save(auth);

        return ResponseEntity.ok("User registered successfully!");
    }

}