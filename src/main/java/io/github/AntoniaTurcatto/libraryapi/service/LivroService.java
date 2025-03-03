package io.github.AntoniaTurcatto.libraryapi.service;

import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import io.github.AntoniaTurcatto.libraryapi.repository.LivroRepository;
import io.github.AntoniaTurcatto.libraryapi.repository.specs.LivroSpecs;
import io.github.AntoniaTurcatto.libraryapi.validator.LivroValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final LivroValidator livroValidator;

    public LivroService(LivroRepository livroRepository, LivroValidator livroValidator){
        this.livroRepository= livroRepository;
        this.livroValidator = livroValidator;
    }

    public Livro salvar(Livro livro){
        livroValidator.validar(livro);
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){
        return livroRepository.findById(id);
    }
    public void deletar(UUID id){
        livroRepository.deleteById(id);
    }

    //isbn, titulo, nome autor, genero, ano de publicação
    public Page<Livro> pesquisa(String isbn,
                                String titulo,
                                String nomeAutor,
                                GeneroLivro genero,
                                Integer anoDePublicacao,
                                Integer pagina,
                                Integer tamanhoPagina){

        //root = dados que eu quero da query
        //query = obj do criqueria query

        //INICIALIZANDO A SPECT PARA DEPOIS VER QUAL CAMPO FOI INFORMADO PELO USUARIO
        //select * from livro where 0 = 0 (apenas para inicializar o where)
        Specification<Livro> specs = Specification.where((root, query, cb)
                -> cb.conjunction()); //conjunction = criterio verdadeiro

        if (isbn != null){
            //query += isbn = :isbn
            System.out.println("livro com isbn");
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if (titulo != null){
            System.out.println("livro com titulo");
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if(genero != null){
            System.out.println("livro com genero");
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }

        if(anoDePublicacao != null){
            specs = specs.and(LivroSpecs.anoPublicacaoEqual(anoDePublicacao));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);
        return livroRepository.findAll(specs,pageRequest);
    }

    public void atualizar(Livro livro){
        if (livro.getId() == null){
            throw new IllegalArgumentException("Para atualizar, é necessário que o livro ja esteja salvo na base");
        }
        livroValidator.validar(livro);
        livroRepository.save(livro);
    }
}
