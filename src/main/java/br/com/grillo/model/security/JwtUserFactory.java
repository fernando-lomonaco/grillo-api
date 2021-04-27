package br.com.grillo.model.security;

import br.com.grillo.enums.RoleType;
import br.com.grillo.model.Auth;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUserFactory {

    public static JwtUser create(Auth auth) {
        return new JwtUser(auth.getCode(),
                auth.getUsername(),
                auth.getEmail(),
                auth.getPassword(),
                createGrantedAuthorities(auth.getRole()));
    }

    private static List<GrantedAuthority> createGrantedAuthorities(RoleType role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }
}
