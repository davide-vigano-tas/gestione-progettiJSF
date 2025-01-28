package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class Timesheet implements Serializable {
	private static final long serialVersionUID = 7654336974232692517L;

	private long id;
	private long idDipendente;
	private long idProgetto;
	private long idTask;
	private double oreLavorate;
	private Date data;
	private Boolean approvato;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdDipendente() {
		return idDipendente;
	}

	public void setIdDipendente(long idDipendente) {
		this.idDipendente = idDipendente;
	}

	public long getIdProgetto() {
		return idProgetto;
	}

	public void setIdProgetto(long idProgetto) {
		this.idProgetto = idProgetto;
	}

	public long getIdTask() {
		return idTask;
	}

	public void setIdTask(long idTask) {
		this.idTask = idTask;
	}

	public double getOreLavorate() {
		return oreLavorate;
	}

	public void setOreLavorate(double oreLavorate) {
		this.oreLavorate = oreLavorate;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Boolean isApprovato() {
		System.out.println(approvato);
		return approvato;
	}

	public void setApprovato(Boolean approvato) {
		this.approvato = approvato;
	}

	@Override
	public int hashCode() {
		return Objects.hash(approvato, data, id, idDipendente, idProgetto, idTask, oreLavorate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timesheet other = (Timesheet) obj;
		return approvato == other.approvato && Objects.equals(data, other.data) && id == other.id
				&& idDipendente == other.idDipendente && idProgetto == other.idProgetto && idTask == other.idTask
				&& Double.doubleToLongBits(oreLavorate) == Double.doubleToLongBits(other.oreLavorate);
	}

	@Override
	public String toString() {
		return "Timesheet [id=" + id + ", id_dipendente=" + idDipendente + ", id_progetto=" + idProgetto + ", id_task="
				+ idTask + ", ore_lavorate=" + oreLavorate + ", data=" + data + ", approvato=" + approvato + "]";
	}
}
