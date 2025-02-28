package io.github.AntoniaTurcatto.libraryapi.controller.mappers;

import io.github.AntoniaTurcatto.libraryapi.config.model.Livro;
import io.github.AntoniaTurcatto.libraryapi.config.model.Autor;
import io.github.AntoniaTurcatto.libraryapi.controller.dto.CadastroLivroDTO;
import io.github.AntoniaTurcatto.libraryapi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class LivroMapper {//abstract class para poder implementar a lógica de injetar o Autor

    @Autowired
    AutorRepository autorRepository;

    @Mapping(source = "generoLivro", target = "genero")
    @Mapping(target = "autor", expression = "java(" +
                "autorRepository.findById(" +
                    "dto.idAutor()" +
                ").orElse(null)" +
            ")")
    public abstract Livro toEntity(CadastroLivroDTO dto);

}
