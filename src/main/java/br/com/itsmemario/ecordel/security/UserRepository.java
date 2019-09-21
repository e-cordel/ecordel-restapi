package br.com.itsmemario.ecordel.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<ECorderlUser, Long>{
	
	Optional<ECorderlUser> findByUsername(String username);

}
