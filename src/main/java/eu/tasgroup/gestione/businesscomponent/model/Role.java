package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Objects;

import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;

public class Role implements Serializable {

	private static final long serialVersionUID = 6040595092040718356L;

	private long id;
	private Ruoli role;
	private long idUser;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Ruoli getRole() {
		return role;
	}

	public void setRole(Ruoli role) {
		this.role = role;
	}

	public long getIdUser() {
		return idUser;
	}

	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, idUser, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		return id == other.id && idUser == other.idUser && role == other.role;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", role=" + role + ", idUser=" + idUser + "]";
	}

}
