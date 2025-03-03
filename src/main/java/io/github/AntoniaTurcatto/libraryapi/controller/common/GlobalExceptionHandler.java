package io.github.AntoniaTurcatto.libraryapi.controller.common;

import io.github.AntoniaTurcatto.libraryapi.controller.dto.ErroCampo;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.ErroRespostaDTO;
import io.github.AntoniaTurcatto.libraryapi.exceptions.CampoInvalidoException;
import io.github.AntoniaTurcatto.libraryapi.exceptions.OperacaoNaoPermitidaException;
import io.github.AntoniaTurcatto.libraryapi.exceptions.RegistroDuplicadoException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice //capturar exceptions e retornar response
//serve para não poluir o código com try-catchs repetidos
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

    @ExceptionHandler(RegistroDuplicadoException.class)//erro a ser tratado
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroRespostaDTO handleRegistroDuplicadoException(RegistroDuplicadoException e){
        return ErroRespostaDTO.conflito(e.getMessage());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroRespostaDTO handleOperacaoNaoPermitidaException(OperacaoNaoPermitidaException e){
        return ErroRespostaDTO.respostaPadrao(e.getMessage());
    }

    @ExceptionHandler(CampoInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErroRespostaDTO handleCampoInvalidoException(CampoInvalidoException e){
        return new ErroRespostaDTO(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Erro de validação",
                List.of(new ErroCampo(e.getCampo(), e.getMessage()))
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroRespostaDTO handleErrosNaoTratados(RuntimeException e){
        e.printStackTrace();
        return new ErroRespostaDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado. Entre em contato com a administração do sistema.",
                List.of());
    }
}
