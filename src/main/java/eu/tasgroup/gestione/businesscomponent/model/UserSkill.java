package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Objects;

public class UserSkill implements Serializable {

	private static final long serialVersionUID = -988738152236513943L;

	private long id;
	private long idCompetenze;
	private long idUtente;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdCompetenze() {
		return idCompetenze;
	}

	public void setIdCompetenze(long idCompetenze) {
		this.idCompetenze = idCompetenze;
	}

	public long getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(long idUtente) {
		this.idUtente = idUtente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idCompetenze, idUtente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserSkill other = (UserSkill) obj;
		return id == other.id && idCompetenze == other.idCompetenze && idUtente == other.idUtente;
	}

	@Override
	public String toString() {
		return "UserSkill [id=" + id + ", id_competenze=" + idCompetenze + ", id_utente=" + idUtente + "]";
	}

}
