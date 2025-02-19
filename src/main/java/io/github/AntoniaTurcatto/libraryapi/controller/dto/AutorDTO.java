package io.github.AntoniaTurcatto.libraryapi.controller.dto;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;

import java.time.LocalDate;

public record AutorDTO(String nome,
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
