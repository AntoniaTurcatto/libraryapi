package io.github.AntoniaTurcatto.libraryapi.validator;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.exceptions.RegistroDuplicadoException;
import io.github.AntoniaTurcatto.libraryapi.repository.AutorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AutorValidator {

    private AutorRepository autorRepository;

    public AutorValidator(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public void validar(Autor a){
        if (existeAutorCadastrado(a)){
            throw new RegistroDuplicadoException("Autor já cadastrado");
        }
    }

    private boolean existeAutorCadastrado(Autor autor){
        Optional<Autor> autorOp = autorRepository
                .findByNomeAndDataNascimentoAndNacionalidade(
                        autor.getNome(),
                        autor.getDataNascimento(),
                        autor.getNacionalidade()
                );

        return (autor.getId() == null) //cadastro
                ? autorOp.isPresent() //cadastro
                : autorOp.isPresent() && autor.getId() != autorOp.get().getId();//atualizacao
        //atualizacao(se autor foi encontrado e se o autor encontrado é diferente do autor a ser atualizado
        //senalizando o mesmo autor com id diferente, o que seria uma duplicação)
    }
}
