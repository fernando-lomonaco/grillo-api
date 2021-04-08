package br.com.grillo.service;

import br.com.grillo.model.Auth;

import java.util.Optional;

public interface UserService {

    Optional<Auth> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Auth save(Auth auth);
}
