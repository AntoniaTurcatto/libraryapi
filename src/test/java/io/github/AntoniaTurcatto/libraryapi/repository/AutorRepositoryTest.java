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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

    @Autowired
    LivroRepository livroRepository;

//     public AutorRepositoryTest(AutorRepository autorRepository){
//         this.autorRepository = autorRepository;
//     }

     @Test
    public void salvarTest(){
        Autor autor = new Autor();
        autor.setNome("Maria");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951,1,31));

        var autorSalvo = autorRepository.save(autor);
        System.out.println("Autor salvo: "+autorSalvo);
    }

    @Test
    public void atualizarTest(){
        UUID uuid = UUID.fromString("dd25312f-20f1-4501-8557-10ec52fe4d72");
        Optional<Autor> possivelAutor = autorRepository.findById(uuid);

        if(possivelAutor.isPresent()){
            Autor autorEncontrado = possivelAutor.get();
            System.out.println("Dados do autor: " + autorEncontrado);
            autorEncontrado.setDataNascimento(LocalDate.of(1960,1,30));
            autorRepository.save(autorEncontrado);
        } else {
            System.out.println("Autor não encontrado!");
        }

    }

    @Test
    public void listarTodos(){
        List<Autor> lista = autorRepository.findAll();
        lista.forEach(System.out::println);
    }

    @Test
    public void countTest(){
        System.out.println("Contagem de autores: "+autorRepository.count());
    }

    @Test
    public void deletePorIdTest(){
         var id = UUID.fromString("dd25312f-20f1-4501-8557-10ec52fe4d72");
         autorRepository.deleteById(id);
    }

    @Test
    public void deleteTest(){
        var id = UUID.fromString("b5dda154-15db-4a78-9162-8d16644e7b1e");
        var maria = autorRepository.findById(id).get();

        autorRepository.delete(maria);
    }

    @Test
    void salvarAutorComLivroTest(){
        Autor autor = new Autor();
        autor.setNome("Antônio");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1971,6,11));

        Livro livro = new Livro();
        livro.setIsbn("101010");
        livro.setPreco(BigDecimal.valueOf(1080));
        livro.setGenero(GeneroLivro.BIOGRAFIA);
        livro.setTitulo("Rogerio");
        livro.setDataPublicacao(LocalDate.of(1990,11,3));

        Livro livro2 = new Livro();
        livro2.setIsbn("132");
        livro2.setPreco(BigDecimal.valueOf(80));
        livro2.setGenero(GeneroLivro.CIENCIA);
        livro2.setTitulo("Ciensando");
        livro2.setDataPublicacao(LocalDate.of(1890,11,3));

        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);

        autorRepository.save(autor);

        for (Livro l : autor.getLivros()) {
            l.setAutor(autor);
        }

        livroRepository.saveAll(autor.getLivros());
    }

    @Test
    //@Transactional
    void listarLivrosAutor(){
        var autor = autorRepository.findById(UUID.fromString("c8353b47-8134-41c8-a9d0-bb72a91f4cd4"));
        //como é lazy por padrão o OneToMany, ele não carrega a lista junto
        //para isso, deve colocar @Transactional ou criar uma query no repository com join


        //com Query Method (opção ideal sem o @Transactional):
        autor.get().setLivros(livroRepository.findByAutor(autor.get()));

        for (Livro livro : autor.get().getLivros()) {
            System.out.println(livro.toString());
        }

    }
}

