package eu.tasgroup.gestione.businesscomponent.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@ViewScoped
@Named
//Se li voglio in xml
@XmlRootElement(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = -2509070297267526051L;

	private long id;
	private String nome;
	private String cognome;
	private String username;
	private String password;
	private String email;
	private int tentativiFalliti;
	private boolean locked;
	private LocalDateTime dataCreazione;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	@XmlElement
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}
	@XmlElement
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getUsername() {
		return username;
	}
	@XmlElement
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}
	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}

	public int getTentativiFalliti() {
		return tentativiFalliti;
	}

	public void setTentativiFalliti(int tentativiFalliti) {
		this.tentativiFalliti = tentativiFalliti;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public LocalDateTime getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(LocalDateTime dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cognome, dataCreazione, email, id, locked, nome, password, tentativiFalliti, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(dataCreazione, other.dataCreazione)
				&& Objects.equals(email, other.email) && id == other.id && locked == other.locked
				&& Objects.equals(nome, other.nome) && Objects.equals(password, other.password)
				&& tentativiFalliti == other.tentativiFalliti && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", nome=" + nome + ", cognome=" + cognome + ", username=" + username + ", password="
				+ password + ", email=" + email + ", tentativiFalliti=" + tentativiFalliti + ", locked=" + locked
				+ ", dataCreazione=" + dataCreazione + "]";
	}

}
