package eu.tasgroup.gestione.businesscomponent.facade;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.AuditLogBC;
import eu.tasgroup.gestione.businesscomponent.PaymentBC;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.ProjectTaskBC;
import eu.tasgroup.gestione.businesscomponent.SkillBC;
import eu.tasgroup.gestione.businesscomponent.TicketBC;
import eu.tasgroup.gestione.businesscomponent.TimesheetBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class AdminFacade {

	private static AdminFacade af;
	private UserBC userBC;
	private ProjectBC projectBC;
	private PaymentBC paymentBC;
	private ProjectTaskBC projectTaskBC;
	private SkillBC skillBC;
	private TimesheetBC timesheetBC;
	private AuditLogBC auditLogBC;
	private TicketBC ticketBC;
	private AdminFacade() throws DAOException, NamingException {
		
		userBC = new UserBC();
		projectBC = new ProjectBC();
		paymentBC = new PaymentBC();
		projectTaskBC = new ProjectTaskBC();
		skillBC = new SkillBC();
		timesheetBC = new TimesheetBC();
		auditLogBC = new AuditLogBC();
		ticketBC = new TicketBC();
	}
	
	public static AdminFacade getInstance() throws DAOException, NamingException {
		if(af == null) 
			af = new AdminFacade();
		return af;
	}
	
	public User createUser(User user) throws DAOException, NamingException {
		return userBC.createOrUpdate(user);
	}
	
	/*------------------------------------Tutti gli utenti*/
	public User[] getAllUsers() throws DAOException, NamingException {
		return userBC.getAll();
	}
	
	/*------------------------------------Update imformazioni admin*/
	public User updateAdmin(User user) throws DAOException, NamingException {
		User retrieved = userBC.getByUsername(user.getUsername());
		Role[] ruoli = userBC.getRolesById(retrieved.getId());
		if(ruoli.length > 0 && Arrays.asList(ruoli).stream().anyMatch((r) -> r.getRole().equals(Ruoli.ADMIN))) {
			return userBC.createOrUpdate(user);
		} else throw new DAOException(new SQLException("Wrong role"));
	}
	/*------------------------------------Utente in base all'id*/
	public User getUserById(long id) throws DAOException, NamingException {
		return userBC.getById(id);
	}
	
	/*-------------------------------Utente in base allo username*/
	public User getByUsername(String username) throws DAOException, NamingException {
		return userBC.getByUsername(username);
	}
	/*-------------------------------Utente in base alla mail*/
	public User getByEmail(String email) throws DAOException, NamingException {
		return userBC.getByEmail(email);
	}
	
	/*-------------------------------Dipendenti in base alla skill*/
	public User[] getDipendentiBySkill(Skill skill) throws DAOException, NamingException {
		return userBC.getBySkill(skill);
	}
	
	/*-------------------------------Utenti in base al ruolo*/
	public User[] getUsersByRole(Ruoli ruolo) throws DAOException, NamingException {
		return userBC.getByRole(ruolo);
	}
	/*--------------------------- lista dei dipendenti non assegnati */
	public User[] getDipendentiNonAssegnati() throws DAOException, NamingException {
		return userBC.getDipendentiNonAssegnati();
	}

	
	/*-------------------------------Aggiunge una skill al dipendente*/
	public void addSkill(User user, Skill skill) throws DAOException, NamingException {
		Role[] ruoli = userBC.getRolesByUsername(user.getUsername());
		if(Arrays.asList(ruoli).stream().anyMatch( r -> r.getRole().equals(Ruoli.DIPENDENTE))) {
			userBC.addSkill(user, skill);
		} else throw new DAOException(new SQLException("Non puoi associare una skill a un non dipendente"));
	}
	/*-------------------------------Aggiunge un ruolo all'utente*/
	public void addRole(User user, Role role) throws DAOException, NamingException {
		Role[] ruoli = userBC.getRolesByUsername(user.getUsername());
		if(!Arrays.asList(ruoli).stream().anyMatch( r -> r.getRole().equals(role.getRole()))) {
			userBC.addRole(user, role);
		} 
	}
	
	
	/*-------------------------------Modifica un ruolo dell'utente*/
	public void updateRole(Role from, Ruoli to) throws DAOException, NamingException {
		Role[] ruoli = userBC.getRolesById(from.getIdUser());
		if(!Arrays.asList(ruoli).stream().anyMatch( r -> r.getRole().equals(to))) {
			userBC.updateRole(from, to);
		}  else throw new DAOException(new SQLException("Ruolo già assegnato"));
	}
	
	/*--------------------------------Ruoli di un utente*/
	public Role[] getRolesByUsername(String username) throws DAOException, NamingException {
		return userBC.getRolesByUsername(username);
	}
	/*--------------------------------Ruoli di un utente*/
	public Role[] getRolesById(long id) throws DAOException, NamingException {
		return userBC.getRolesById(id);
	}
	
	/*--------------------------------Elimina il ruolo associato all'utente*/
	public void deleteRole(Ruoli role, User user) throws DAOException, NamingException {
		userBC.deleteRole(role, user);
	}
	
	/*----------------------------------Elimina utente*/
	public void deleteUser(User user) throws DAOException, NamingException {
		 userBC.delete(user);
	}
	
	
	/*--------------------------------------Elenco timesheet*/
	public Timesheet[] getAllTimesheet() throws DAOException, NamingException {
		return timesheetBC.getAll();
	}
	
	/*--------------------------------------Timesheet in base all'id*/
	public Timesheet getTimesheetById(long id) throws DAOException, NamingException {
		return timesheetBC.getById(id);
	}
	
	/*--------------------------------------Elenco timesheet assiocati a un dipendente*/
	public List<Timesheet> getTimesheetsByDipendente(long id) throws DAOException, NamingException {
		return timesheetBC.getListByDipendente(id);
	}
	
	/*--------------------------------------Elenco timesheet assiocati a un progetto*/
	public List<Timesheet> getTimesheetsByProject(long id) throws DAOException, NamingException {
		return timesheetBC.getListByProject(id);
	}
	
	/*--------------------------------------Creazione skill*/
	public Skill createSkill(Skill skill) throws DAOException, NamingException {
		return skillBC.create(skill);
	}
	/*-------------------------------------- skill in base all'id*/
	public Skill getSkillById(long id) throws DAOException, NamingException {
		return skillBC.getByID(id);
	}
	/*--------------------------------------Elenco skill*/
	public Skill[] getAllSkills() throws DAOException, NamingException {
		return skillBC.getByAll();
	}
	/*--------------------------------------Elenco skill in base al tipo*/
	public Skill[] getSkillsByTipo(Skills skill) throws DAOException, NamingException {
		return skillBC.getByTipo(skill);
	}
	/*--------------------------------------Elenco skill in base al dipendente*/
	public Skill[] getSkillsByDipendente(User user) throws DAOException, NamingException {
		return skillBC.getByUser(user.getId());
	}
	
	/*---------------------------------Elimina skill*/
	public void deleteSkill(Skill skill) throws DAOException, NamingException {
		skillBC.delete(skill.getId());
	}
	/*---------------------------------Elimina skill*/
	public void deleteSkillByTipo(Skills skill) throws DAOException, NamingException {
		skillBC.deleteByTipo(skill);
	}
	/*----------------------------------Task in base all'id*/
	public ProjectTask getTaskById(long id) throws DAOException, NamingException {
		return projectTaskBC.getByID(id);
	}
	
	/*-----------------------------------Elenco task in base al progetto*/
	public List<ProjectTask> getTasksByProject(Project project) throws DAOException, NamingException {
		return projectTaskBC.getByProject(project.getId());
	}
	
	/*-----------------------------------Elenco task in base al dipendente*/
	public List<ProjectTask> getTasksByDipendente(User user) throws DAOException, NamingException {
		return projectTaskBC.getByDipendente(user.getId());
	}
	/*-----------------------------------Crea o modifca il projetto*/
	public Project createOrUpdateProject(Project project) throws DAOException, NamingException {
		return projectBC.createOrUpdate(project);
	}
	
	/*---------------------------Elimina progetto*/
	public void deleteProject(Project project) throws DAOException, NamingException {
		projectBC.delete(project.getId());
	}
	
	/*--------------------------Percentuale completamento progetto*/
	public int getPercentualeCompletamento(Project project) throws DAOException, NamingException {
		return projectBC.getPercentualeCompletamento(project.getId());
	}
	
	/*--------------------------------Elenco progetto*/
	public Project[] getAllProjects() throws DAOException, NamingException {
		return projectBC.getAll();
	}
	
	/*--------------------------------Progetto in base all'id*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		return projectBC.getById(id);
	}
	/*--------------------------------Elenco progetti in base allo stato*/
	public List<Project> getProjectByStatus(StatoProgetto stato) throws DAOException, NamingException {
		return projectBC.getListProjectByStatus(stato);
	}
	
	/*--------------------------------Elenco progetti in base al cliente*/
	public List<Project> getProjectByCliente(User cliente) throws DAOException, NamingException {
		return projectBC.getListProjectByCliente(cliente.getId());
	}
	
	/*--------------------------------Elenco progetti in base allo stato*/
	public List<Project> getProjectByResp(User res) throws DAOException, NamingException {
		return projectBC.getListProjectByResponsabile(res.getId());
	}
	
	/*-----------------------------Elenco pagamenti*/
	public Payment[] getAllPayments() throws DAOException, NamingException {
		return paymentBC.getAll();
	}
	/*-----------------------------Pagamento in base all'id*/
	public Payment getPaymentById(long id) throws DAOException, NamingException {
		return paymentBC.getById(id);
	}
	
	/*-----------------------------Elimina pagamento*/
	public void deletePayment(Payment payment) throws DAOException, NamingException {
	    paymentBC.delete(payment.getId());
	}
	
	/*-----------------------------Elenco pagamenti di un progetto*/
	public Payment[] getPaymentsByProject(Project project) throws DAOException, NamingException {
		return paymentBC.getByProject(project);
	}
	/*-----------------------------Elenco pagamenti di un cliente*/
	public Payment[] getPaymentsByCliente(User cliente) throws DAOException, NamingException {
		return paymentBC.getByUser(cliente);
	}
	/*-----------------------------Elenco auditlog*/
	public AuditLog[] getAllAuditLogs() throws DAOException, NamingException {
		return auditLogBC.getAll();
	}
	
	/*-------------------------------Update Auditlog*/
	public void createOrupdateAuditLog(AuditLog log) throws DAOException, NamingException {
		auditLogBC.createOrUpdate(log);
	}
	
	/*-------------------------------Delete auditLog*/
	public void deleteAuditLog(AuditLog log) throws DAOException, NamingException {
		auditLogBC.delete(log.getId());
	}
	
	public AuditLog getAuditLogById(long id) throws DAOException, NamingException {
		return auditLogBC.getById(id);
	}
	
	/*------------------------------Ticket*/
	
	public Ticket[] getAllTicket() throws DAOException, NamingException {
		return ticketBC.getAll();
	}
	
	public Ticket getTicketById(long id) throws DAOException, NamingException {
		return ticketBC.getById(id);
	}
	
	public Ticket[] getAllOpen() throws DAOException, NamingException {
		return ticketBC.getAllOpen();
	}
	
	public Ticket[] getAllClosed() throws DAOException, NamingException {
		return ticketBC.getAllClosed();
	}
	
	public Ticket[] getByDipendente(long id) throws DAOException, NamingException {
		return ticketBC.getByDipendente(id);
	}
	
	public void closeTicket(long id) throws DAOException, NamingException {
		ticketBC.closeTicket(id);
	}
	
	public void deleteTicket(long id) throws DAOException, NamingException {
		ticketBC.deleteTicket(id);
	}
}

