package io.github.AntoniaTurcatto.libraryapi.config.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "autor", schema = "public") //nome da tabela e schema, se for public não precisa explicitá-lo
@Getter//gera os getters e setters em compile time
@Setter
@ToString(exclude = "livros")
@EntityListeners(AuditingEntityListener.class)//vai ficar escutando toda vez que tiver
// uma alteração na entidade e fazer as alterações do @CreatedDate e @LastModifiedDate
//isso funciona com @EnableJpaAuditing ativado na classe Application
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

    @OneToMany(mappedBy = "autor")//mappedBy: qual é o nome dessa propriedade de autor na entidade de livros
    private List<Livro> livros;//um autor para muitos livros

    @CreatedDate
    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
