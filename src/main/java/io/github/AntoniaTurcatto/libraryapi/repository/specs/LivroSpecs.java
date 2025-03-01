package io.github.AntoniaTurcatto.libraryapi.repository.specs;

import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import org.springframework.data.jpa.domain.Specification;

public class LivroSpecs {

    public static Specification<Livro> isbnEqual(String isbn){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("isbn"), isbn);
    }

    public static Specification<Livro> tituloLike(String titulo){
        return (root, query, cb)
                -> cb.like(
                        cb.upper(root.get("titulo")),
                        "%"+titulo.toUpperCase()+"%" //para comparar em qualquer pedaço da string
        );
    }

    public static Specification<Livro> generoEqual(GeneroLivro genero){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("genero"), genero);
    }

}
