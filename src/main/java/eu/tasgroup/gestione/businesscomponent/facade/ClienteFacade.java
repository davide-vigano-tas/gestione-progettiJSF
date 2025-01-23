package eu.tasgroup.gestione.businesscomponent.facade;

import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.AuditLogBC;
import eu.tasgroup.gestione.businesscomponent.PaymentBC;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class ClienteFacade {
	
	private static ClienteFacade cf;
	private UserBC userBC;
	private ProjectBC projectBC;
	private PaymentBC paymentBC;
	private AuditLogBC auditLogBC;
	
	private ClienteFacade() throws DAOException, NamingException {
		userBC = new UserBC();
		projectBC = new ProjectBC();
		paymentBC = new PaymentBC();
		auditLogBC = new AuditLogBC();
	}
	
	public static ClienteFacade getInstance() throws DAOException, NamingException {
		if(cf == null) 
			cf = new ClienteFacade();
		return cf;
	}
	
	/*------------------------Crea o modifica il cliente*/
	public User createOrUpdateCliente(User user) throws DAOException, NamingException {
		User created = userBC.createOrUpdate(user);
		if(user.getId() == 0) {
			Role role = new Role();
			role.setRole(Ruoli.CLIENTE);
			userBC.addRole(created, role);
		}
		return created;
		
	}
	
	/*-------------------------------Cliente in base all'id*/
	public User getById(long id) throws DAOException, NamingException {
		return userBC.getById(id);
	}
	/*-------------------------------Cliente in base allo username*/
	public User getByUsername(String username) throws DAOException, NamingException {
		return userBC.getByUsername(username);
	}
	/*-------------------------------Cliente in base alla mail*/
	public User getByEmail(String email) throws DAOException, NamingException {
		return userBC.getByEmail(email);
	}
	
	/*------------------------------------Percentuale completamento progetto in base all'id*/
	public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
		return projectBC.getPercentualeCompletamento(id);
	}
	
	/*--------------------------------------Progetto in base all'ID*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		return projectBC.getById(id);
	}
	/*--------------------------------------Progetti associati al cliente*/
	public List<Project> getProjectsByCliente(User user) throws DAOException, NamingException {
		return projectBC.getListProjectByCliente(user.getId());
	}

	/*--------------------------------------Instaziamento del pagamento*/
  	public void createPayment(Payment payment) throws DAOException, NamingException {
  		 paymentBC.create(payment);
  	}

  	/*--------------------------------------Pagamento in base all'id*/
  	public Payment getPaymentById(long id) throws DAOException, NamingException {
  		return paymentBC.getById(id);
  	}
  	/*--------------------------------------Pagamenti in base al progetto*/
  	public Payment[] getPaymentByProject(Project project) throws DAOException, NamingException {
  		return paymentBC.getByProject(project);
  	}
  	/*--------------------------------------Pagamenti in base al cliente*/
  	public Payment[] getPaymentByCliente(User cliente) throws DAOException, NamingException {
  		return paymentBC.getByUser(cliente);
  	}
  	
  	/*--------------------------------Ruoli di un utente*/
	public Role[] getRolesById(long id) throws DAOException, NamingException {
		return userBC.getRolesById(id);
	}
	
	public double getTotalPaymentsByProjectId(long id)throws DAOException, NamingException {
		Project p = projectBC.getById(id);
		Payment[] payments = paymentBC.getByProject(p);
		
		double totale = 0;
		
		for(Payment payment : payments) {
			totale += payment.getCifra();
		}
		
		return totale;
	}
	
	/*-------------------------------Update Auditlog*/
	public void saveLogMessage(String username, String operazione) throws DAOException, NamingException {
		
		AuditLog log = new AuditLog();
		
		log.setUtente(username);
		log.setOperazione(operazione);
		log.setData(new Date());
		
		auditLogBC.createOrUpdate(log);
	}
}
