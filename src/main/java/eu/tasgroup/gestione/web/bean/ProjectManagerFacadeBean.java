package eu.tasgroup.gestione.web.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.AuditLogBC;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.ProjectTaskBC;
import eu.tasgroup.gestione.businesscomponent.TicketBC;
import eu.tasgroup.gestione.businesscomponent.TimesheetBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;
@Named
@RequestScoped
public class ProjectManagerFacadeBean {

	private static ProjectManagerFacadeBean pmf;
	private UserBC userBC;
	private ProjectBC projectBC;
	private ProjectTaskBC projectTaskBC;
	private TimesheetBC timesheetBC;
	private TicketBC ticketBC;
	private AuditLogBC auditLogBC;

	private ProjectManagerFacadeBean() throws DAOException, NamingException {
		ticketBC = new TicketBC();
		auditLogBC = new AuditLogBC();
	}

	public static ProjectManagerFacadeBean getInstance() throws DAOException, NamingException {
		if (pmf == null)
			pmf = new ProjectManagerFacadeBean();

		return pmf;
	}

	/*------------------------Crea o modifica il Project Manager*/
	public User createOrUpdateProjectManager(User user) throws DAOException, NamingException {
		userBC = new UserBC();
		User created = userBC.createOrUpdate(user);
		if (user.getId() == 0) {
			Role role = new Role();
			role.setRole(Ruoli.PROJECT_MANAGER);
			userBC.addRole(created, role);
		}
		return created;
	}

	/*-------------------------------Project Manager in base all'id*/
	public User getProjectManagerById(long id) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getById(id);
	}

	/*-------------------------------Project Manager in base allo username*/
	public User getProjectManagerByUsername(String username) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getByUsername(username);
	}

	/*-------------------------------Project Manager in base alla email*/
	public User getProjectManagerByEmail(String email) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getByEmail(email);
	}

	// -------------------- array di clienti sulla base del ruolo
	public User[] getByRole(Ruoli role) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getByRole(role);
	}

	public User[] getBySkill(Skill skill) throws DAOException, NamingException {
		userBC = new UserBC();
		return userBC.getBySkill(skill);
	}

	/*--------------------------- lista dei dipendenti non assegnati */
	public User[] getDipendentiNonAssegnati() throws DAOException, NamingException {
		return userBC.getDipendentiNonAssegnati();
	}

	/*------------------------------- Gestione dei progetti --------------------------------*/

	/*------------------------------- Crea o aggiorna un progetto */
	public Project createOrUpdateProject(Project project) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.createOrUpdate(project);
	}

	/*------------------------------------Percentuale completamento progetto in base all'id*/
	public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.getPercentualeCompletamento(id);
	}

	/*--------------------------------------Progetto in base all'ID*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.getById(id);
	}

	/*--------------------------------------Progetti associati al Project Manager*/
	public List<Project> getProjectsByResponsabile(User user) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.getListProjectByResponsabile(user.getId());
	}

	/*--------------------------------------Progetti associati al cliente*/
	public List<Project> getProjectsByCliente(User user) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.getListProjectByCliente(user.getId());
	}

	/*------------------------------- Gestione delle task --------------------------------*/

	public ProjectTask createOrUpdateProjectTask(ProjectTask pt) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.createOrUpdate(pt);
	}

	public ProjectTask updateProjectTaskFase(Fase fase, long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.updateFase(fase, id);
	}

	public ProjectTask updateProjectTaskStato(StatoTask stato, long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.updateStato(stato, id);
	}

	public void deleteProjectTask(long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		projectTaskBC.delete(id);
	}

	public ProjectTask getProjectTaskByID(long id) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getByID(id);
	}

	public ProjectTask[] getAllProjectTask() throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getAll();
	}

	public List<ProjectTask> getListProjectTaskByDipendente(long idDip) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getByDipendente(idDip);
	}

	public List<ProjectTask> getListProjectTaskByProject(long idProject) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getByProject(idProject);
	}

	public List<ProjectTask> getListProjectTaskByProjectManager(long idProjectManager)
			throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		return projectTaskBC.getByProjectManager(idProjectManager);
	}

	public List<ProjectTask> getTaskByFaseAndProject(Fase fase, long idProject) throws DAOException, NamingException {
		projectTaskBC = new ProjectTaskBC();
		List<ProjectTask> tasks = new ArrayList<ProjectTask>();
		for (ProjectTask task : projectTaskBC.getByProject(idProject)) {
			if (task.getFase() == fase)
				tasks.add(task);
		}
		return tasks;
	}

	/*------------------------------- Gestione dei timesheet --------------------------------*/

	public Timesheet getTimesheetById(long id) throws DAOException, NamingException {
		timesheetBC = new TimesheetBC();
		return timesheetBC.getById(id);
	}

	public List<Timesheet> getTimesheetByManager(long id) throws DAOException, NamingException {
		timesheetBC = new TimesheetBC();
		return timesheetBC.getListByProjectManager(id);
	}

	public void approvaTimesheet(long id, boolean stato) throws DAOException, NamingException {
		timesheetBC = new TimesheetBC();
		timesheetBC.approva(id, stato);
	}

	public Timesheet[] getAllTimesheet() throws DAOException, NamingException {
		timesheetBC = new TimesheetBC();
		return timesheetBC.getAll();
	}

	/*--------------------------------Ruoli di un utente*/
	public Role[] getRolesById(long id) throws DAOException, NamingException {
		return userBC.getRolesById(id);
	}

	public void updatePercentualeCompletamentoProjectID(long idProgetto, int n) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		Project project = projectBC.getById(idProgetto);
		project.setPercentualeCompletamento(n);
		projectBC.createOrUpdate(project);
	}
	
	/*----------------------------------------Ticket*/
	
	public void createorUpdateTicket(Ticket ticket) throws DAOException, NamingException {
		ticketBC.createOrUpdate(ticket);
	}
	
	public Ticket[] getByDipendente(long id) throws DAOException, NamingException {
		return ticketBC.getByDipendente(id);
	}
	
	public Ticket getTicketById(long id) throws DAOException, NamingException {
		return ticketBC.getById(id);
	}
	
	//-------------------------------
	public Set<User> getDipendentiByBroject(long idProject) throws DAOException, NamingException{
		List<ProjectTask> tasks = projectTaskBC.getByProject(idProject);
		 Set<User> dipendenti = new HashSet<User>();
		for(ProjectTask task : tasks) {
			dipendenti.add(userBC.getById(task.getIdDipendente()));
		}
		return dipendenti;
	}
	
	public void createOrupdateAuditLog(AuditLog log) throws DAOException, NamingException {
		auditLogBC.createOrUpdate(log);
	}
}
