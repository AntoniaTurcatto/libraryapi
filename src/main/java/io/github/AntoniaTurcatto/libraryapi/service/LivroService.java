package io.github.AntoniaTurcatto.libraryapi.service;

import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import io.github.AntoniaTurcatto.libraryapi.repository.LivroRepository;
import io.github.AntoniaTurcatto.libraryapi.repository.specs.LivroSpecs;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository){
        this.livroRepository= livroRepository;
    }

    public Livro salvar(Livro livro){
        return livroRepository.save(livro);
    }

    public Optional<Livro> obterPorId(UUID id){
        return livroRepository.findById(id);
    }
    public void deletar(UUID id){
        livroRepository.deleteById(id);
    }

    //isbn, titulo, nome autor, genero, ano de publicação
    public List<Livro> pesquisa(String isbn,
                                String titulo,
                                String nomeAutor,
                                GeneroLivro genero,
                                Integer anoDePublicacao){

        //select * from livro where isbn = :isbn and nomeAutor = ....
//        Specification<Livro> specs = Specification.
//                where(LivroSpecs.isbnEqual(isbn))
//                .and(LivroSpecs.tituloLike(titulo))
//                .and(LivroSpecs.generoEqual(genero))
//                ;
        //root = dados que eu quero da query
        //query = obj do criqueria query

        //INICIALIZANDO A SPECT PARA DEPOIS VER QUAL CAMPO FOI INFORMADO PELO USUARIO
        //select * from livro where 0 = 0 (apenas para inicializar o where)
        Specification<Livro> specs = Specification.where((root, query, cb)
                -> cb.conjunction()); //conjunction = criterio verdadeiro

        if (isbn != null){
            //query += isbn = :isbn
            specs = specs.and(LivroSpecs.isbnEqual(isbn));
        }

        if (titulo != null){
            specs = specs.and(LivroSpecs.tituloLike(titulo));
        }

        if(genero != null){
            specs = specs.and(LivroSpecs.generoEqual(genero));
        }

        return livroRepository.findAll(LivroSpecs.isbnEqual(isbn));
    }
}
