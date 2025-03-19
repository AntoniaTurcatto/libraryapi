package io.github.AntoniaTurcatto.libraryapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //RestController é para API, Controller é para paginas web
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin(){ //é retornado String da pagina que ele tem que ir ao chamar a requisição
        return "login"; //pagina referenciada no WebConfigurator
    }

}
