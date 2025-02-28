package io.github.AntoniaTurcatto.libraryapi.controller.mappers;

import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.AutorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")//transforma em componente Spring para podermos fazer D.I.
public interface AutorMapper {

    //para caso o nome das variáveis seja diferente na origem e no destino
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "dataNascimento", target = "dataNascimento")
    Autor toEntity(AutorDTO dto);

    //caso o nome seja o mesmo, não é necessário mapear na mão assim
    AutorDTO toDTO(Autor autor);
}
