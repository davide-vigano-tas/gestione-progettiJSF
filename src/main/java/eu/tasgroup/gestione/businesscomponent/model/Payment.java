package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Objects;

public class Payment implements Serializable{


	private static final long serialVersionUID = 2636460469019243963L;

	private long id;
	private long idProgetto;
	private double cifra;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdProgetto() {
		return idProgetto;
	}
	public void setIdProgetto(long idProgetto) {
		this.idProgetto = idProgetto;
	}
	public double getCifra() {
		return cifra;
	}
	public void setCifra(double cifra) {
		this.cifra = cifra;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cifra, id, idProgetto);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Payment other = (Payment) obj;
		return Double.doubleToLongBits(cifra) == Double.doubleToLongBits(other.cifra) && id == other.id
				&& idProgetto == other.idProgetto;
	}
	@Override
	public String toString() {
		return "Payment [id=" + id + ", id_progetto=" + idProgetto + ", cifra=" + cifra + "]";
	}
	
	
}
