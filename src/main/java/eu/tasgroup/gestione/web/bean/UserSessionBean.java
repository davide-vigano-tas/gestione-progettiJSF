package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;

@Named
@SessionScoped
public class UserSessionBean implements Serializable {

	private static final long serialVersionUID = 4924291759062947883L;

	private String username;
	private List<Role> roles;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	
	public boolean hasRole(Ruoli role) {
        return roles.stream().anyMatch(r -> r.getRole().equals(role));
    }
}
