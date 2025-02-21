package io.github.AntoniaTurcatto.libraryapi.controller.dto;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;

import java.time.LocalDate;
import java.util.UUID;

public record AutorDTO(
       UUID id,
       String nome,
       LocalDate dataNascimento,
       String nacionalidade) {

    public Autor mapearParaAutor(){
        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setDataNascimento(dataNascimento);
        autor.setNacionalidade(nacionalidade);
        return autor;
    }




}
