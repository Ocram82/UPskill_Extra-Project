package pt.upskil.desafio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Ronda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int numero;
    @OneToOne
    private Pergunta pergunta;
    @OneToOne
    private Resposta respostaEscolhida;
    @ManyToOne
    private Jogo jogo;

    public Ronda() {
    }
}
