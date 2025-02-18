package io.github.AntoniaTurcatto.libraryapi.repository;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void salvarTest(){
        Livro livro = new Livro();
        livro.setIsbn("3123123");
        livro.setPreco(BigDecimal.valueOf(108));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("UFO");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951,1,31));

        var autorSalvo = autorRepository.save(autor);

        livro.setAutor(autorSalvo);

        System.out.println("Livro salvo: "+ livroRepository.save(livro));

    }

    @Test
    void salvarCascadeTest(){
        Livro livro = new Livro();
        livro.setIsbn("3123123");
        livro.setPreco(BigDecimal.valueOf(108));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("UFO");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        Autor autor = new Autor();
        autor.setNome("Joana");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1971,4,29));



        livro.setAutor(autor);

        System.out.println("Livro salvo: "+ livroRepository.save(livro));

    }

    @Test
    void atualizarAutorLivro(){
        var livroParaAtualizar = livroRepository
                .findById(UUID.fromString("e8345d7c-b8bf-4568-8bfe-481320f012f5")).orElse(null);

        var autorNovo = autorRepository.findById(UUID.fromString("4811f0ee-9f55-4a30-a279-78a621c5e94f"));
        livroParaAtualizar.setAutor(autorNovo.get());

        livroRepository.save(livroParaAtualizar);
    }

    @Test
    void deletar(){
        var livroParaDeletar = livroRepository
                .findById(UUID.fromString("e8345d7c-b8bf-4568-8bfe-481320f012f5")).orElse(null);

        if(livroParaDeletar != null){
            livroRepository.delete(livroParaDeletar);
        }
    }

    @Test
    @Transactional
    void buscarLivroTest(){
        Livro livro = livroRepository.findById(
                        UUID.fromString("3cc9fcbf-f01b-4554-96dc-d3cc345643f0"))
                .orElse(null);

        System.out.println("Livro: "+ livro.getTitulo());
        System.out.println("    Autor: "+ livro.getAutor().getNome());
        System.out.println();
    }
}