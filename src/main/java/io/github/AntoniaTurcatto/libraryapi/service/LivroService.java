package io.github.AntoniaTurcatto.libraryapi.service;

import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import io.github.AntoniaTurcatto.libraryapi.repository.LivroRepository;
import org.springframework.stereotype.Service;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository){
        this.livroRepository= livroRepository;
    }

    public Livro salvar(Livro livro){
        return livroRepository.save(livro);
    }

}
