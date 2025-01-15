package api_futbol.com.api_futbol.models;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_estadio")
@EqualsAndHashCode(of = "id")
public class Estadio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = false)
    private Integer capacidade;

    private Boolean estado;

    public Estadio() {
    }
}
