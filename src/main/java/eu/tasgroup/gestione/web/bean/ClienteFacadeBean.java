package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.Role;
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

	@Inject
	private UserSessionBean userSessionBean;

	public ClienteFacadeBean() throws DAOException, NamingException {
		cf = ClienteFacade.getInstance();
	}

	@PostConstruct
	public void init() {
		try {
			this.user = getByUsername(userSessionBean.getUsername());
			this.projects = getProjectsByCliente(user);
			this.payment = new Payment();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
		}
	}

	public User createOrUpdateCliente(User user) throws DAOException, NamingException {
		return cf.createOrUpdateCliente(user);
	}
	public String createCliente(User user) throws DAOException, NamingException {
		cf.createOrUpdateCliente(user);
		return "login?i=2&faces-redirect=true";
	}

	public User getById(long id) throws DAOException, NamingException {
		return cf.getById(id);
	}

	public User getByUsername(String username) throws DAOException, NamingException {
		return cf.getByUsername(username);
	}

	public User getByEmail(String email) throws DAOException, NamingException {
		return cf.getByEmail(email);
	}

	public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
		return cf.getPercentualeCompletamentoProjectID(id);
	}

	public Project getProjectById(long id) throws DAOException, NamingException {
		return cf.getProjectById(id);
	}

	public List<Project> getProjectsByCliente(User user) throws DAOException, NamingException {
		return cf.getProjectsByCliente(user);
	}

	public void createPayment(Payment payment) throws DAOException, NamingException {
		cf.createPayment(payment);
	}

	public Payment getPaymentById(long id) throws DAOException, NamingException {
		return cf.getPaymentById(id);
	}

	public Payment[] getPaymentByProject(Project project) throws DAOException, NamingException {
		return cf.getPaymentByProject(project);
	}

	public Payment[] getPaymentByCliente(User cliente) throws DAOException, NamingException {
		return cf.getPaymentByCliente(cliente);
	}

	public Role[] getRolesById(long id) throws DAOException, NamingException {
		return cf.getRolesById(id);
	}

	public double getTotalPaymentsByProjectId(long id) throws DAOException, NamingException {
		return cf.getTotalPaymentsByProjectId(id);
	}

	public void saveLogMessage(String username, String operazione) throws DAOException, NamingException {
		cf.saveLogMessage(username, operazione);
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
	
	public void confirmPayment() throws DAOException, NamingException {
		cf.createPayment(payment);
	}
	
	public void preparePaymentModal(Project project) {
	    this.payment = new Payment(); // O un'istanza preesistente
	    this.payment.setIdProgetto(project.getId());
	    this.payment.setCifra(project.getCostoProgetto());
	    this.project = project; // Per nome e altri dettagli
	}

}
