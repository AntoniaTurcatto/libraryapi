package io.github.AntoniaTurcatto.libraryapi.controller;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.AutorDTO;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.ErroRespostaDTO;
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
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService){
        this.autorService = autorService;
    }

    @PostMapping //@Valid = testa as anotações de validação do AutorDTO
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autorDTO){//? = coringa/wildcard
        try {
            var autorEntidade = autorDTO.mapearParaAutor();
            autorService.salvar(autorEntidade);
            //http://localhost:8080/autores/42543154365terq534
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(autorEntidade.getId())
                    .toUri(); //pegando a url atual e adicionando o ID

            return ResponseEntity.created(location).build();
        } catch (RegistroDuplicadoException e) {
            e.printStackTrace();
            var erroDTO = ErroRespostaDTO.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id){ //via url
        var idAutor = UUID.fromString(id);
        Optional<Autor> autor = autorService.obterPorId(idAutor);
        if(autor.isPresent()){
            AutorDTO dto = new AutorDTO(idAutor,
                    autor.get().getNome(),
                    autor.get().getDataNascimento(),
                    autor.get().getNacionalidade());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable String id){
        try {

            var idAutor = UUID.fromString(id);
            Optional<Autor> autorOp = autorService.obterPorId(idAutor);
            if(autorOp.isPresent()){
                autorService.deletar(autorOp.get());
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();

        }catch (OperacaoNaoPermitidaException e){
            var erroResposta = ErroRespostaDTO.respostaPadrao(e.getMessage());
            return ResponseEntity.status(erroResposta.status()).body(erroResposta);
        }
    }

    @GetMapping//UTILIZA O VALUE PORQUE NÃO É REQUIRED E TEM MAIS DE 1
    public ResponseEntity<List<AutorDTO>> pesquisar(@RequestParam(value = "nome", required = false) String nome,
                                                    @RequestParam(value = "nacionalidade", required = false) String nacionalidade){
         List<Autor> listAutor = autorService.pesquisaByExample(nome, nacionalidade);
         List<AutorDTO> listDto = listAutor.stream().map(autor ->
             new AutorDTO(autor.getId(),
                 autor.getNome(),
                 autor.getDataNascimento(),
                 autor.getNacionalidade())
         ).toList();

         return ResponseEntity.ok(listDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable("id") String id,
                                          @RequestBody @Valid AutorDTO dto){
        try{
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
        } catch (RegistroDuplicadoException e) {
            e.printStackTrace();
            var erroDTO = ErroRespostaDTO.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }

    }

}
