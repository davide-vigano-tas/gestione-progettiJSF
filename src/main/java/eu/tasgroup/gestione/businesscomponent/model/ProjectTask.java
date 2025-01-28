package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
@Named
@ViewScoped
public class ProjectTask implements Serializable {
	private static final long serialVersionUID = 4442416040038390835L;

	private long id;
	private long idProgetto;
	private String nomeTask;
	private String descrizione;
	private long idDipendente;
	private StatoTask stato;
	private Date scadenza;
	private Fase fase;

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

	public String getNomeTask() {
		return nomeTask;
	}

	public void setNomeTask(String nomeTask) {
		this.nomeTask = nomeTask;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public long getIdDipendente() {
		return idDipendente;
	}

	public void setIdDipendente(long idDipendente) {
		this.idDipendente = idDipendente;
	}

	public StatoTask getStato() {
		return stato;
	}

	public void setStato(StatoTask stato) {
		this.stato = stato;
	}

	public Date getScadenza() {
		return scadenza;
	}

	public void setScadenza(Date scadenza) {
		this.scadenza = scadenza;
	}

	public Fase getFase() {
		return fase;
	}

	public void setFase(Fase fase) {
		this.fase = fase;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descrizione, fase, id, idDipendente, idProgetto, nomeTask, scadenza, stato);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectTask other = (ProjectTask) obj;
		return Objects.equals(descrizione, other.descrizione) && fase == other.fase && id == other.id
				&& idDipendente == other.idDipendente && idProgetto == other.idProgetto
				&& Objects.equals(nomeTask, other.nomeTask) && Objects.equals(scadenza, other.scadenza)
				&& stato == other.stato;
	}

	@Override
	public String toString() {
		return "ProjectTask [id=" + id + ", id_progetto=" + idProgetto + ", nome_task=" + nomeTask + ", descrizione="
				+ descrizione + ", id_dipendente=" + idDipendente + ", stato=" + stato + ", scadenza=" + scadenza
				+ ", fase=" + fase + "]";
	}

}
