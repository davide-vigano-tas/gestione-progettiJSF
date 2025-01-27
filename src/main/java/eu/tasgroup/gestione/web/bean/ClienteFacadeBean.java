package eu.tasgroup.gestione.web.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.User;

@Named
@SessionScoped
public class ClienteFacadeBean implements Serializable {

	private static final long serialVersionUID = 4615242297710114833L;

	private ClienteFacade cf;
	private User user;
	private Project project;
	private Payment payment;
	private List<Project> projects;
	private List<Payment> payments;
	private List<Payment> paymentsUser;

	private boolean success;
	private boolean error;

	@Inject
	private UserSessionBean userSessionBean;

	public ClienteFacadeBean() throws DAOException, NamingException {
		cf = ClienteFacade.getInstance();
	}

	@PostConstruct
	public void init() {
		try {
			this.user = cf.getByUsername(userSessionBean.getUsername());
			this.projects = cf.getProjectsByCliente(user);
			this.payment = new Payment();
			this.paymentsUser = Arrays.asList(cf.getPaymentByCliente(user));

		} catch (DAOException | NamingException e) {
			e.printStackTrace();
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) throws DAOException, NamingException {
		this.projects = projects;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public List<Payment> getPaymentsUser() {
		return paymentsUser;
	}

	public void setPaymentsUser(List<Payment> paymentsUser) {
		this.paymentsUser = paymentsUser;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public User createOrUpdateCliente(User user) throws DAOException, NamingException {
		return cf.createOrUpdateCliente(user);
	}

	public String createCliente(User user) throws DAOException, NamingException {
		cf.createOrUpdateCliente(user);
		return "login?i=2&faces-redirect=true";
	}

	public void confirmPayment() {
		try {
			double maxPagamento = cf.getProjectById(payment.getIdProgetto()).getCostoProgetto()
					- cf.getTotalPaymentsByProjectId(payment.getIdProgetto());

			if (maxPagamento <= 0 || maxPagamento - payment.getCifra() < 0) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(
						"/" + UserSessionBean.getServletContextName() + "/cliente/cliente-home.xhtml?error=true");
				return;
			}

			cf.createPayment(payment);
			FacesContext.getCurrentInstance().getExternalContext().redirect(
					"/" + UserSessionBean.getServletContextName() + "/cliente/cliente-home.xhtml?success=true");
		} catch (DAOException | NamingException e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(
						"/" + UserSessionBean.getServletContextName() + "/cliente/cliente-home.xhtml?error=true");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showPaymentHistory(long projectID) {
		try {
			// Recupera i dettagli del progetto e i pagamenti
			Project p = cf.getProjectById(projectID);
			payments = Arrays.asList(cf.getPaymentByProject(p));
			System.out.println("Pagamenti caricati: " + payments);

			// Redirezione alla pagina lista-pagamenti
			FacesContext.getCurrentInstance().getExternalContext()
					.redirect("/" + UserSessionBean.getServletContextName() + "/cliente/lista-pagamenti.xhtml");
		} catch (DAOException | NamingException | IOException e) {
			e.printStackTrace();
		}
	}

	public double calcolaDaVersare(long projectID) {
		try {
			return cf.getProjectById(projectID).getCostoProgetto() - cf.getTotalPaymentsByProjectId(projectID);
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	public void displayMessage() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    if (success) {
	    	success = false;
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
	            "Pagamento Confermato", "Il pagamento è stato registrato con successo."));
	    }
	    if (error) {
	    	error = false;
	        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
	            "Pagamento Rifiutato", "Il pagamento supera il limite massimo o non è consentito."));
	    }
	}

}
