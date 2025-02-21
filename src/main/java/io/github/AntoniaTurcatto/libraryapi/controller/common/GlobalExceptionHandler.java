package io.github.AntoniaTurcatto.libraryapi.controller.common;

import io.github.AntoniaTurcatto.libraryapi.controller.dto.ErroCampo;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.ErroRespostaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice //capturar exceptions e retornar response
public class GlobalExceptionHandler {

    //retornar o que queremos na resposta da exceção
    @ExceptionHandler(MethodArgumentNotValidException.class)
    //toda vez que essa exceção ocorrer, virá para cá
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) //sempre retornará esse status ao chegar nesse ExceptionHandler
    public ErroRespostaDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        System.out.println(e);
        List<FieldError> fieldErrors = e.getFieldErrors(); //campos que foram erro de validação

        List<ErroCampo> listErrors = fieldErrors.stream().map(fe -> new ErroCampo(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return new ErroRespostaDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                listErrors);
    }

}
