package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;

public class Project implements Serializable {

	private static final long serialVersionUID = -1080423990767730075L;

	private long id;
	private String nomeProgetto;
	private String descrizione;
	private Date dataInizio;
	private Date dataFine;
	private double budget;
	private StatoProgetto stato;
	private long idCliente;
	private long idResponsabile;
	private int percentualeCompletamento;
	private double costoProgetto;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNomeProgetto() {
		return nomeProgetto;
	}

	public void setNomeProgetto(String nomeProgetto) {
		this.nomeProgetto = nomeProgetto;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}

	public Date getDataFine() {
		return dataFine;
	}

	public void setDataFine(Date dataFine) {
		this.dataFine = dataFine;
	}

	public double getBudget() {
		return budget;
	}

	public void setBudget(double budget) {
		this.budget = budget;
	}

	public StatoProgetto getStato() {
		return stato;
	}

	public void setStato(StatoProgetto stato) {
		this.stato = stato;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public long getIdResponsabile() {
		return idResponsabile;
	}

	public void setIdResponsabile(long idResponsabile) {
		this.idResponsabile = idResponsabile;
	}

	public int getPercentualeCompletamento() {
		return percentualeCompletamento;
	}

	public void setPercentualeCompletamento(int percentualeCompletamento) {
		this.percentualeCompletamento = percentualeCompletamento;
	}

	public double getCostoProgetto() {
		return costoProgetto;
	}

	public void setCostoProgetto(double costoProgetto) {
		this.costoProgetto = costoProgetto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(budget, costoProgetto, dataFine, dataInizio, descrizione, id, idCliente, idResponsabile,
				nomeProgetto, percentualeCompletamento, stato);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		return Double.doubleToLongBits(budget) == Double.doubleToLongBits(other.budget)
				&& Double.doubleToLongBits(costoProgetto) == Double.doubleToLongBits(other.costoProgetto)
				&& Objects.equals(dataFine, other.dataFine) && Objects.equals(dataInizio, other.dataInizio)
				&& Objects.equals(descrizione, other.descrizione) && id == other.id && idCliente == other.idCliente
				&& idResponsabile == other.idResponsabile && Objects.equals(nomeProgetto, other.nomeProgetto)
				&& percentualeCompletamento == other.percentualeCompletamento && stato == other.stato;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", nomeProgetto=" + nomeProgetto + ", descrizione=" + descrizione + ", dataInizio="
				+ dataInizio + ", dataFine=" + dataFine + ", budget=" + budget + ", stato=" + stato + ", idCliente="
				+ idCliente + ", idResponsabile=" + idResponsabile + ", percentualeCompletamento="
				+ percentualeCompletamento + ", costoProgetto=" + costoProgetto + "]";
	}

}
