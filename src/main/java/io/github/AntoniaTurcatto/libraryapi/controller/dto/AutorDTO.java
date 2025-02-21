package io.github.AntoniaTurcatto.libraryapi.controller.dto;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
       UUID id,

       @NotBlank(message = "campo obrigatório")
       @Size(min = 1, max = 100, message = "campo fora do tamanho padrão")

       String nome,

       @NotNull(message = "campo obrigatório")
       @Past(message = "data inválida")
       LocalDate dataNascimento,

       @NotBlank(message = "campo obrigatório")
       @Size(min = 3, max = 50, message = "campo fora do tamanho padrão")
       String nacionalidade) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setDataNascimento(dataNascimento);
        autor.setNacionalidade(nacionalidade);
        return autor;
    }




}
