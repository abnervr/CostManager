package org.abner.manager.model.posicao;

import org.abner.manager.model.AbstractModel;

public class Cidade extends AbstractModel {

	private Estado estado;

	private String nome;

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
