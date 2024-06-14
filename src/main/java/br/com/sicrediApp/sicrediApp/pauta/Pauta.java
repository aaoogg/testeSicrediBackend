/**
 * Author: Antônio Oscar Gehrke
 */

package br.com.sicrediApp.sicrediApp.pauta;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int votosSim;
    private int votosNao;
    private boolean pautaEmVotacao;
    private boolean votacaoFinalizada;

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

	public int getVotosSim() {
		return votosSim;
	}

	public void setVotosSim(int votosSim) {
		this.votosSim = votosSim;
	}

	public int getVotosNao() {
		return votosNao;
	}

	public void setVotosNao(int votosNao) {
		this.votosNao = votosNao;
	}

	public boolean getPautaEmVotacao() {
		return pautaEmVotacao;
	}

	public void setPautaEmVotacao(boolean pautaEmVotacao) {
		this.pautaEmVotacao = pautaEmVotacao;
	}

	public boolean getVotacaoFinalizada() {
		return votacaoFinalizada;
	}

	public void setVotacaoFinalizada(boolean votacaoFinalizada) {
		this.votacaoFinalizada = votacaoFinalizada;
	}
}
