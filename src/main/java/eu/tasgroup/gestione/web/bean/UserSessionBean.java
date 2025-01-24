package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;

@Named
@SessionScoped
public class UserSessionBean implements Serializable {



	private static final long serialVersionUID = 4924291759062947883L;

	private String username;
	private String password;
	private String userType;

	private List<String> roles = new ArrayList<String>();
	
	public UserSessionBean() {
		roles.add(Ruoli.ADMIN.name());
		roles.add(Ruoli.CLIENTE.name());
		roles.add(Ruoli.DIPENDENTE.name());
		roles.add(Ruoli.PROJECT_MANAGER.name());
	}

	public List<String> getRoles() {
		return roles;
	}



	public void setRoles(List<String> roles) {
		this.roles = roles;
	} 
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		System.err.println("Used??");
	}


	
	public boolean hasRole(Ruoli role) throws DAOException, NamingException {
		if(username != null) {
			AdminFacadeBean userBean = new AdminFacadeBean();
			User user = userBean.getByUsername(username);
			List<Role> roles = Arrays.asList(userBean.getRolesById(user.getId()));
	        return roles.stream().anyMatch(r -> r.getRole().equals(role));
		} 
		return false;
    }
	

	public String validate() {
		try {
			AdminFacadeBean userBean = new AdminFacadeBean();
			User user = userBean.getByUsername(username);
			System.err.println("Login?");
			if(user!=null) {
				if(Algoritmo.verificaPassword(password, user.getPassword())) {
					
					List<Role> roles = Arrays.asList(userBean.getRolesById(user.getId()));
					if(roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE)) && userType.equals(Ruoli.CLIENTE.name())) {
						return "cliente/cliente-home";

						
					}
					if(roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE)) && userType.equals(Ruoli.DIPENDENTE.name())) {
				

						return 	"dipendente/dipendente-home";
					} if(roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER)) && userType.equals(Ruoli.PROJECT_MANAGER.name())) {
						
						return "projectManager/projectManager-home";
					} if(roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN)) && userType.equals(Ruoli.ADMIN.name())) {
						
						return "admin/admin-home";
					} 
					return "login?i=2&faces-redirect=true";
				} else {
					FacesContext fc = FacesContext.getCurrentInstance();
					fc.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_WARN,
									"Incorrect Username and Password",
									"Please enter correct username and Password"));
					fc.renderResponse();
				}
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "login?i=2&faces-redirect=true";
	}
	
	public boolean isLogged() {
		return username != null;
	}
	
	public void isLoggedRedirect() {
	    if (username != null) {
	        FacesContext context = FacesContext.getCurrentInstance();
	        NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();
	       
	    	if(userType.equals(Ruoli.CLIENTE.name())) {
				 navigationHandler.handleNavigation(context, null, "cliente/cliente-home");
			}
			if(userType.equals(Ruoli.DIPENDENTE.name())) {
		
				 navigationHandler.handleNavigation(context, null, "dipendente/dipendente-home");
			} if(userType.equals(Ruoli.PROJECT_MANAGER.name())) {
				
				 navigationHandler.handleNavigation(context, null, "projectManager/projectManager-home");
			} if(userType.equals(Ruoli.ADMIN.name())) {
				
				 navigationHandler.handleNavigation(context, null, "admin/admin-home");
			} 
	    }
	}
	
	public void isNotLoggedRedirect() {
		if(username == null) {
	        FacesContext context = FacesContext.getCurrentInstance();
	        NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();
	        navigationHandler.handleNavigation(context, null, "login?i=2&faces-redirect=true");
		}
	}
	
	public String logout() {
		username=null;
		setPassword(null);
		return "login?i=2&faces-redirect=true";
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
