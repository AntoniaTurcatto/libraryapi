package io.github.AntoniaTurcatto.libraryapi.controller;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.config.model.Usuario;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.AutorDTO;
import io.github.AntoniaTurcatto.libraryapi.controller.mappers.AutorMapper;
import io.github.AntoniaTurcatto.libraryapi.security.SecurityService;
import io.github.AntoniaTurcatto.libraryapi.service.AutorService;
import io.github.AntoniaTurcatto.libraryapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<?> salvar(@RequestBody @Valid AutorDTO autorDTO
                                    ){//pode colocar Authentication como argumento para auditoria de qual foi o usuario que fez a modificação


        var autorEntidade = mapper.toEntity(autorDTO);
        autorService.salvar(autorEntidade);
        //http://localhost:8080/autores/42543154365terq534
        URI location = gerarHeaderLocation(autorEntidade.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
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
    @PreAuthorize("hasRole('GERENTE')")
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
    @PreAuthorize("hasAnyRole('OPERADOR', 'GERENTE')")
    public ResponseEntity<List<AutorDTO>> pesquisar(@RequestParam(value = "nome", required = false) String nome,
                                                    @RequestParam(value = "nacionalidade", required = false) String nacionalidade){
         List<Autor> listAutor = autorService.pesquisaByExample(nome, nacionalidade);
         List<AutorDTO> listDto = listAutor.stream().map(
                 mapper::toDTO
         ).toList();

         return ResponseEntity.ok(listDto);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
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
