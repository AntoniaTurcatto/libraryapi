package io.github.AntoniaTurcatto.libraryapi.repository;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AutorRepositoryTest {

    @Autowired
    AutorRepository autorRepository;

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
}

