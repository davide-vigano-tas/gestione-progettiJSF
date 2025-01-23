package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Ticket implements Serializable{


	private static final long serialVersionUID = -89848559536084427L;
	
	private long id;
	private long opener;
	private String title;
	private String description;
	private Date created_at;
	private Date closed_at;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOpener() {
		return opener;
	}
	public void setOpener(long opener) {
		this.opener = opener;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getClosed_at() {
		return closed_at;
	}
	public void setClosed_at(Date closed_at) {
		this.closed_at = closed_at;
	}
	@Override
	public int hashCode() {
		return Objects.hash(created_at, description, id, opener, title);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		return Objects.equals(created_at, other.created_at) && Objects.equals(description, other.description)
				&& id == other.id && opener == other.opener && Objects.equals(title, other.title);
	}
	@Override
	public String toString() {
		return "Ticket [id=" + id + ", opener=" + opener + ", title=" + title + ", description=" + description
				+ ", created_at=" + created_at + "]";
	}
	
	

}
