package io.github.AntoniaTurcatto.libraryapi.service;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import io.github.AntoniaTurcatto.libraryapi.repository.AutorRepository;
import io.github.AntoniaTurcatto.libraryapi.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class TransacaoService {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public void executar(){

        Autor autor = new Autor();
        autor.setNome("Francisca");
        autor.setNacionalidade("Brasileira");
        autor.setDataNascimento(LocalDate.of(1951,1,31));

        autorRepository.save(autor);

        Livro livro = new Livro();
        livro.setIsbn("3123123");
        livro.setPreco(BigDecimal.valueOf(108));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("FRFRFRFFF");
        livro.setDataPublicacao(LocalDate.of(1980,1,2));

        livro.setAutor(autor);

        System.out.println("Livro salvo: "+ livroRepository.save(livro));

        if(!autor.getNome().equals("Francisca")){
            throw new RuntimeException("Rollback");
        }
    }

    @Transactional
    public void atualizacaoSemAtualizar(){
        var livro = livroRepository
                .findById(UUID.fromString("0bf6b782-72ae-4d69-9dfb-bdd39435d6ee"))
                .orElse(null);
        livro.setDataPublicacao(LocalDate.of(2024,6,1));

        //livroRepository.save(livro); não é necessário, pois após o find, ja está no estado Managed

    }
}
