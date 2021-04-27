package br.com.grillo.service.security;

import br.com.grillo.model.Auth;
import br.com.grillo.model.security.JwtUserFactory;
import br.com.grillo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Auth auth = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return JwtUserFactory.create(auth);
    }

    public Optional<Auth> findByUsername(String name) {
        return userService.findByUsername(name);
    }
}