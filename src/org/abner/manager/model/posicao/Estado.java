package org.abner.manager.model.posicao;

import org.abner.manager.model.AbstractModel;

public class Estado extends AbstractModel {

	private Pais pais;

	private String uf;

	private String nome;

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
