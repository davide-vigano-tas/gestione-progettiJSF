package eu.tasgroup.gestione.web.bean;

import java.io.IOException;
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
import javax.servlet.ServletContext;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;
import eu.tasgroup.gestione.businesscomponent.utility.EmailUtil;
import eu.tasgroup.gestione.businesscomponent.utility.OTPUtil;

@Named
@SessionScoped
public class UserSessionBean implements Serializable {

	private static final long serialVersionUID = 4924291759062947883L;

	private String username;
	private String password;
	private String userType;
	private String otp; // Campo per l'OTP inserito dall'utente
	private String generatedOtp; // Campo per memorizzare l'OTP generato
	private String userEmail; // Campo per memorizzare l'email dell'utente

	private List<String> roles = new ArrayList<String>();

	public UserSessionBean() {
		roles.add(Ruoli.ADMIN.name());
		roles.add(Ruoli.CLIENTE.name());
		roles.add(Ruoli.DIPENDENTE.name());
		roles.add(Ruoli.PROJECT_MANAGER.name());
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getGeneratedOtp() {
		return generatedOtp;
	}

	public void setGeneratedOtp(String generatedOtp) {
		this.generatedOtp = generatedOtp;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
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
	}

	public boolean hasRole(Ruoli role) throws DAOException, NamingException {
		if (username != null) {
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
			if (user != null) {
				if (Algoritmo.verificaPassword(password, user.getPassword())) {
					List<Role> roles = Arrays.asList(userBean.getRolesById(user.getId()));
					if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE))
							&& userType.equals(Ruoli.CLIENTE.name())) {

						// Memorizza l'email dell'utente
						this.userEmail = user.getEmail();

						// Genera l'OTP
						generatedOtp = OTPUtil.generateOTP();

						String emailContent = "<!DOCTYPE html><html lang=\"en\">"
								+ "<head><meta charset=\"UTF-8\"></head>" + "<body>"
								+ "<div style='background-color:#f4f4f4;padding:20px;'>"
								+ "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
								+ "<h1 style='text-align:center;color:#007bff;'>Verifica il tuo accesso</h1>"
								+ "<p style='text-align:center;'>Il tuo codice OTP è:</p>"
								+ "<h2 style='text-align:center;color:#007bff;'>" + generatedOtp + "</h2>"
								+ "<p style='text-align:center;'>Questo codice è valido per 5 minuti.</p>"
								+ "</div></div></body></html>";

						// Invia l'OTP via email
						EmailUtil.sendEmail(userEmail, "Your OTP Code", emailContent);

						// Reindirizza alla pagina di verifica OTP
						return "otp-verification?faces-redirect=true";
						// return "cliente/cliente-home";

					}
					if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))
							&& userType.equals(Ruoli.DIPENDENTE.name())) {

						return "dipendente/dipendente-home";
					}
					if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))
							&& userType.equals(Ruoli.PROJECT_MANAGER.name())) {

						return "projectManager/projectManager-home";
					}
					if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))
							&& userType.equals(Ruoli.ADMIN.name())) {

						return "admin/admin-home";
					}
					return "login?faces-redirect=true";
				} else {
					FacesContext fc = FacesContext.getCurrentInstance();
					fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Username and Password",
							"Please enter correct username and Password"));
					fc.renderResponse();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "login?faces-redirect=true";
	}

	public String validateOtp() {
		if (otp != null && otp.equals(generatedOtp)) {
			// OTP corretto, procedi con il login
			try {
				AdminFacadeBean userBean = new AdminFacadeBean();
				User user = userBean.getByUsername(username);
				List<Role> roles = Arrays.asList(userBean.getRolesById(user.getId()));

				if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.CLIENTE))
						&& userType.equals(Ruoli.CLIENTE.name())) {
					return "cliente/cliente-home";
				}
				if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))
						&& userType.equals(Ruoli.DIPENDENTE.name())) {
					return "dipendente/dipendente-home";
				}
				if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.PROJECT_MANAGER))
						&& userType.equals(Ruoli.PROJECT_MANAGER.name())) {
					return "projectManager/projectManager-home";
				}
				if (roles.stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))
						&& userType.equals(Ruoli.ADMIN.name())) {
					return "admin/admin-home";
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesContext fc = FacesContext.getCurrentInstance();
				fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore durante il login",
						"Si è verificato un errore durante il login."));
				fc.renderResponse();
			}
		} else {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "OTP non valido", "Inserisci un OTP valido."));
			fc.renderResponse();
		}

		return null; // Resta sulla stessa pagina in caso di errore
	}

	public boolean isLogged() {
		return username != null;
	}

	public void isLoggedRedirect() {
		if (username != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();

			if (userType.equals(Ruoli.CLIENTE.name())) {
				navigationHandler.handleNavigation(context, null, "cliente/cliente-home");
			}
			if (userType.equals(Ruoli.DIPENDENTE.name())) {

				navigationHandler.handleNavigation(context, null, "dipendente/dipendente-home");
			}
			if (userType.equals(Ruoli.PROJECT_MANAGER.name())) {

				navigationHandler.handleNavigation(context, null, "projectManager/projectManager-home");
			}
			if (userType.equals(Ruoli.ADMIN.name())) {

				navigationHandler.handleNavigation(context, null, "admin/admin-home");
			}
		}
	}

	public void isNotLoggedRedirect() throws IOException {
		if (username == null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().redirect("../login.xhtml?faces-redirect=true");
		}
	}

	public void logout() throws IOException {
		username = null;
		setPassword(null);
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().redirect("/" + getServletContextName() + "/login.xhtml?faces-redirect=true");
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static String getServletContextName() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
		return servletContext.getServletContextName();
	}
}
