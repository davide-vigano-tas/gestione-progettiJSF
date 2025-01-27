package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;

@Named
@ViewScoped
public class Skill implements Serializable {

	private static final long serialVersionUID = 4615730567266391294L;

	private long id;
	private Skills tipo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Skills getTipo() {
		return tipo;
	}

	public void setTipo(Skills tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Skill [id=" + id + ", tipo=" + tipo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tipo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Skill other = (Skill) obj;
		return id == other.id && tipo == other.tipo;
	}

}