package io.github.AntoniaTurcatto.libraryapi.controller;

import io.github.AntoniaTurcatto.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.ErroRespostaDTO;
import io.github.AntoniaTurcatto.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.AntoniaTurcatto.libraryapi.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService){
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid CadastroLivroDTO dto){
        try {
            //mapear DTO para entidade

            //enviar a entidade para o service validar e salvar na base

            //criar url para acesso dos dados do livro
            
            //retornar codigo created com header location
            return ResponseEntity.ok(dto);
        } catch (RegistroDuplicadoException e) {
            var erroDTO = ErroRespostaDTO.conflito(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);

        }
    }
}
