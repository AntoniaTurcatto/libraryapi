package io.github.AntoniaTurcatto.libraryapi.repository;

import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
