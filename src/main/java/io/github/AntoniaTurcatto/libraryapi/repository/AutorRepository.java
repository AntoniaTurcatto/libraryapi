package io.github.AntoniaTurcatto.libraryapi.repository;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
}
