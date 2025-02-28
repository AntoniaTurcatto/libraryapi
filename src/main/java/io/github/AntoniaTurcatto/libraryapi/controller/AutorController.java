package io.github.AntoniaTurcatto.libraryapi.controller;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.AutorDTO;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.ErroRespostaDTO;
import io.github.AntoniaTurcatto.libraryapi.controller.mappers.AutorMapper;
import io.github.AntoniaTurcatto.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.AntoniaTurcatto.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.AntoniaTurcatto.libraryapi.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/autores")
//host:8080/autores
public class AutorController implements GenericController{

    private final AutorService autorService;
    private final AutorMapper mapper;

    public AutorController(AutorService autorService, AutorMapper mapper){
        this.autorService = autorService;
        this.mapper=mapper;
    }

    @PostMapping //@Valid = testa as anotações de validação do AutorDTO
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autorDTO){//? = coringa/wildcard
        //var autorEntidade = autorDTO.mapearParaAutor();
        var autorEntidade = mapper.toEntity(autorDTO);
        autorService.salvar(autorEntidade);
        //http://localhost:8080/autores/42543154365terq534
        URI location = gerarHeaderLocation(autorEntidade.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) { //via url
        var idAutor = UUID.fromString(id);
        //refatorando
        //Optional<Autor> autor = autorService.obterPorId(idAutor);
        return  autorService.obterPorId(idAutor)
                .map(autor -> {
                    AutorDTO dto = mapper.toDTO(autor);
                    return ResponseEntity.ok(dto);
                }).orElseGet(() -> ResponseEntity.notFound().build());

//        if(autor.isPresent()){
//           AutorDTO dto = new AutorDTO(idAutor,
//                  autor.get().getNome(),
//                    autor.get().getDataNascimento(),
//                    autor.get().getNacionalidade());
//            autor.get().setId(idAutor);
//            AutorDTO dto = mapper.toDTO(autor.get());
//            return ResponseEntity.ok(dto);
        //}
        //return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOp = autorService.obterPorId(idAutor);
        if(autorOp.isPresent()){
            autorService.deletar(autorOp.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping//UTILIZA O VALUE PORQUE NÃO É REQUIRED E TEM MAIS DE 1
    public ResponseEntity<List<AutorDTO>> pesquisar(@RequestParam(value = "nome", required = false) String nome,
                                                    @RequestParam(value = "nacionalidade", required = false) String nacionalidade){
         List<Autor> listAutor = autorService.pesquisaByExample(nome, nacionalidade);
         List<AutorDTO> listDto = listAutor.stream().map(
                 mapper::toDTO
         ).toList();

         return ResponseEntity.ok(listDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") String id,
                                          @RequestBody @Valid AutorDTO dto){
        var idAutor = UUID.fromString(id);
        Optional<Autor> autorOp = autorService.obterPorId(idAutor);
        if(autorOp.isPresent()){
            autorOp.get().setId(idAutor);
            autorOp.get().setNome(dto.nome());
            autorOp.get().setNacionalidade(dto.nacionalidade());
            autorOp.get().setDataNascimento(dto.dataNascimento());

            autorService.atualizar(autorOp.get());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

}
