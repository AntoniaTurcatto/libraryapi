package io.github.AntoniaTurcatto.libraryapi.repository.specs;

import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
    //select to_char(data_publicacao, 'YYYY') == :anoPublicacao from livro
    public static Specification<Livro> anoPublicacaoEqual(Integer anoPublicacao){
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(
                        criteriaBuilder.function("to_char",
                                String.class,
                                root.get("dataPublicacao"),
                                criteriaBuilder.literal("YYYY")
                        ),anoPublicacao.toString());
    }

    public static Specification<Livro> nomeAutorLike(String nome){
        return (root, query, cb) -> {
            //return cb.like(cb.upper(root.get("autor").get("nome")), "%" + nome.toUpperCase() + "%"); //aqui é sempre inner join
            Join<Object, Object> joinAutor = root.join("autor", JoinType.INNER); //aqui eu posso controlar o join
            return cb.like(cb.upper(joinAutor.get("nome")), "%"+nome+"%");
        };
    }

}
