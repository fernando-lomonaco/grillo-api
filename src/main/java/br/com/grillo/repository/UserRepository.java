package br.com.grillo.repository;

import java.util.Optional;
import java.util.UUID;

import br.com.grillo.model.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Auth, UUID> {

    Optional<Auth> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
}
