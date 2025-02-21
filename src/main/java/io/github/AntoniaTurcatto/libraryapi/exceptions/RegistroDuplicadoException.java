package io.github.AntoniaTurcatto.libraryapi.exceptions;

//exception de dominio
public class RegistroDuplicadoException extends RuntimeException{

    public RegistroDuplicadoException(String message) {
        super(message);
    }
}
