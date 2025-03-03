package io.github.AntoniaTurcatto.libraryapi.validator;

import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import io.github.AntoniaTurcatto.libraryapi.exceptions.CampoInvalidoException;
import io.github.AntoniaTurcatto.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.AntoniaTurcatto.libraryapi.repository.LivroRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LivroValidator {

    private static final int ANO_EXIGENCIA_PRECO = 2020;
    private final LivroRepository livroRepository;

    public LivroValidator(LivroRepository livroRepository){
        this.livroRepository=livroRepository;
    }

    public void validar(Livro livro){
        if (existeLivroComIsbn(livro))
            throw new RegistroDuplicadoException("ISBN já cadastrado");

        if (isPrecoObrigatorioNulo(livro))
            throw new CampoInvalidoException("preco", "Para livros com ano de publicação a patir de 2020, o ano é obrigatório");
    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return livro.getPreco() == null
                && livro.getDataPublicacao().getYear() >= ANO_EXIGENCIA_PRECO;
    }

    private boolean existeLivroComIsbn(Livro livro){
        Optional<Livro> livroOp = livroRepository.findByIsbn(livro.getIsbn());
        if(livro.getId() == null){ //cadastrando
            return  livroOp.isPresent();
        }

        return livroOp
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));


    }
}
