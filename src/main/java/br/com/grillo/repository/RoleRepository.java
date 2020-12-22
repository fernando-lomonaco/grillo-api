package br.com.grillo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.grillo.model.entity.Role;
import br.com.grillo.model.enums.RoleType;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(RoleType name);
    
}