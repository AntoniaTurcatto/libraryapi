package io.github.AntoniaTurcatto.libraryapi.controller.dto;

import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CadastroLivroDTO(String isbn,
                               String titulo,
                               LocalDate dataPublicacao,
                               GeneroLivro generoLivro,
                               BigDecimal preco,
                               UUID idAutor
                               ) {
}
