package io.github.AntoniaTurcatto.libraryapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //RestController é para API, Controller é para paginas web
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin(){ //é retornado String da pagina que ele tem que ir ao chamar a requisição
        return "login"; //pagina referenciada no WebConfigurator
    }
    /*
    Ao acessar essa URL, o méthodo retorna a String "login",
    que é interpretada como o nome da view.
    O mecanismo de resolução de views do Spring procurará por um
    template chamado "login" (por exemplo, um arquivo login.html
    em um diretório de templates configurado).
     */

    @GetMapping("/")
    @ResponseBody//não vai esperar uma página
    public String paginaHome(Authentication auth){
        return "olá "+auth.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code){
        return "Seu authorization code: "+code;
    }

}
