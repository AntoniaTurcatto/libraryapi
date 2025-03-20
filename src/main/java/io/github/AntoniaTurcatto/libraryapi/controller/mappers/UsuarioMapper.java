package io.github.AntoniaTurcatto.libraryapi.controller.mappers;

import io.github.AntoniaTurcatto.libraryapi.config.model.Usuario;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.UsuarioDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    Usuario toEntity(UsuarioDTO dto);

}
