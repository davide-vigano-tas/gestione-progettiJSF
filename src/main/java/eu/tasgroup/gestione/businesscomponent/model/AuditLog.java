package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class AuditLog implements Serializable {
	private static final long serialVersionUID = 195969313819477500L;

	private long id;
	private String utente;
	private String operazione;
	private Date data;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getUtente() {
		return utente;
	}
	public void setUtente(String utente) {
		this.utente = utente;
	}
	public String getOperazione() {
		return operazione;
	}
	public void setOperazione(String operazione) {
		this.operazione = operazione;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	@Override
	public int hashCode() {
		return Objects.hash(data, id, operazione, utente);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditLog other = (AuditLog) obj;
		return Objects.equals(data, other.data) && id == other.id && Objects.equals(operazione, other.operazione)
				&& Objects.equals(utente, other.utente);
	}
	@Override
	public String toString() {
		return "AuditLog [id=" + id + ", utente=" + utente + ", operazione=" + operazione + ", data=" + data + "]";
	}
}
