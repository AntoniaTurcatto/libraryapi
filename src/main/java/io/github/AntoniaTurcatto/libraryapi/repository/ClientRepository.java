package io.github.AntoniaTurcatto.libraryapi.repository;

import io.github.AntoniaTurcatto.libraryapi.config.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findByClientId(String id);
}
