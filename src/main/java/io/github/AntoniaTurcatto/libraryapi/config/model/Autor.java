package io.github.AntoniaTurcatto.libraryapi.config.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autor", schema = "public") //nome da tabela e schema, se for public não precisa explicitá-lo
@Getter//gera os getters e setters em compile time
@Setter
@ToString
public class Autor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)//gera o valor automaticamente
    private UUID id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "nacionalidade", length = 50, nullable = false)
    private String nacionalidade;

    //@OneToMany(mappedBy = "autor")//mappedBy: qual é o nome dessa propriedade de autor na entidade de livros
    @Transient//não é coluna
    private List<Livro> livros;//um autor para muitos livros
}
