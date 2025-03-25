package io.github.AntoniaTurcatto.libraryapi.controller;

import io.github.AntoniaTurcatto.libraryapi.config.model.Client;
import io.github.AntoniaTurcatto.libraryapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('GERENTE')")
    //o ideal é criar o dto e o mapper, mas como ja se sabe fazer não será criado, para focar no conteudo novo
    public void salvar(@RequestBody Client client){
        service.salvar(client);
    }
}
