package io.github.AntoniaTurcatto.libraryapi.controller;

import io.github.AntoniaTurcatto.libraryapi.config.model.GeneroLivro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.ResultadoPesquisaLivroDTO;
import io.github.AntoniaTurcatto.libraryapi.controller.mappers.LivroMapper;
import io.github.AntoniaTurcatto.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/livros")
public class LivroController implements GenericController{

    private final LivroService livroService;
    private final LivroMapper livroMapper;

    public LivroController(LivroService livroService, LivroMapper livroMapper){
        this.livroService = livroService;
        this.livroMapper=livroMapper;
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid CadastroLivroDTO dto){
            //mapear DTO para entidade
            //enviar a entidade para o service validar e salvar na base
            //criar url para acesso dos dados do livro
            //retornar codigo created com header location
            Livro livro = livroMapper.toEntity(dto);
            System.out.println(livro);
            livro = livroService.salvar(livro);
            URI location = gerarHeaderLocation(livro.getId());
            return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultadoPesquisaLivroDTO> obterDetalhes(@PathVariable("id") String id){
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {
                    var dto = livroMapper.toDTO(livro);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") String id){
        return livroService.obterPorId(UUID.fromString(id))
                .map(livro -> {livroService.deletar(livro.getId());
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //isbn, titulo, nome autor, genero, ano de publicação
    @GetMapping
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "nome-autor", required = false) String nomeAutor,
            @RequestParam(value = "genero", required = false) GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false) Integer anoDePublicacao,
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "tamanho-pagina", defaultValue = "10") Integer tamanhoPagina
    ){
        Page<Livro> paginaResultado = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoDePublicacao, pagina, tamanhoPagina);

        Page<ResultadoPesquisaLivroDTO> resultadoDTO = paginaResultado.map(livroMapper::toDTO);

        return ResponseEntity.ok(resultadoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") String id, @RequestBody @Valid CadastroLivroDTO dto){
        return livroService.obterPorId(UUID.fromString(id))
            .map( livro -> {
                Livro entidadeDoDTOAtualizada = livroMapper.toEntity(dto);
                livro.setDataPublicacao(entidadeDoDTOAtualizada.getDataPublicacao());
                livro.setIsbn(entidadeDoDTOAtualizada.getIsbn());
                livro.setPreco(entidadeDoDTOAtualizada.getPreco());
                livro.setGenero(entidadeDoDTOAtualizada.getGenero());
                livro.setTitulo(entidadeDoDTOAtualizada.getTitulo());
                livro.setAutor(entidadeDoDTOAtualizada.getAutor());

                livroService.atualizar(livro);
                return ResponseEntity.noContent().build();
            }

        ).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
