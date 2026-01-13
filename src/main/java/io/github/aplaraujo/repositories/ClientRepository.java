package io.github.aplaraujo.repositories;

import io.github.aplaraujo.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    Boolean existsByEmail(String email);
}
