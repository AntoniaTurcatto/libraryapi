package io.github.AntoniaTurcatto.libraryapi.service;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.AntoniaTurcatto.libraryapi.repository.AutorRepository;
import io.github.AntoniaTurcatto.libraryapi.repository.LivroRepository;
import io.github.AntoniaTurcatto.libraryapi.validator.AutorValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final AutorValidator autorValidator;

    public AutorService(AutorRepository autorRepository,
                        AutorValidator autorValidator,
                        LivroRepository livroRepository){
        this.autorRepository=autorRepository;
        this.livroRepository=livroRepository;
        this.autorValidator = autorValidator;
    }

    public Autor salvar(Autor autor){
        autorValidator.validar(autor);
        return autorRepository.save(autor);
    }

    public Optional<Autor> obterPorId(UUID id){
        return autorRepository.findById(id);
    }

    public void deletar(Autor autor){
        if(possuiLivro(autor))
            throw new OperacaoNaoPermitidaException(
                    "Não é permitido excluir um autor que possui livros cadastrados");
        autorRepository.delete(autor);
    }

    public List<Autor> pesquisa(String nome, String nacionalidade){
        if (nome != null && nacionalidade != null) {
            return autorRepository.findByNomeAndNacionalidade(nome, nacionalidade);

        } else if (nome != null){
            return autorRepository.findByNome(nome);

        } else if (nacionalidade != null){
            return autorRepository.findByNacionalidade(nacionalidade);

        } else {
            return autorRepository.findAll();
        }


    }

    public void atualizar(Autor autor){
        autorValidator.validar(autor);
        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualizar é necessário que o autor ja esteja salvo na base");
        }
        autorRepository.save(autor);
    }

    public boolean possuiLivro(Autor autor){
        return livroRepository.existsByAutor(autor);
    }
}
