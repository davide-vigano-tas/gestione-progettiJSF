package eu.tasgroup.gestione.businesscomponent.facade;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.AuditLogBC;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.ProjectTaskBC;
import eu.tasgroup.gestione.businesscomponent.SkillBC;
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

public class DipendenteFacade {
	
	private static DipendenteFacade df;
	private UserBC userBC;
	private ProjectBC projectBC;
	private TimesheetBC tBC;
	private ProjectTaskBC ptBC;
	private SkillBC sBC;
	private AuditLogBC auditLogBC ;
	private TicketBC ticketBC;
	
	
	private DipendenteFacade() throws DAOException, NamingException {
		
		userBC = new UserBC();
		projectBC = new ProjectBC();
		tBC = new TimesheetBC();
		ptBC = new ProjectTaskBC();
		sBC = new SkillBC();
		ticketBC = new TicketBC();
		auditLogBC = new AuditLogBC();
	}
	
	public static DipendenteFacade getInstance() throws DAOException, NamingException {
		if(df == null) 
			df = new DipendenteFacade();
		return df;
	}
	
	// ---------- crea o modifica dipendente
	public User createOrUpdateDipendente(User user) throws DAOException, NamingException {
		User created = userBC.createOrUpdate(user);
		if(user.getId() == 0) {
			Role role = new Role();
			role.setRole(Ruoli.DIPENDENTE);
			userBC.addRole(created, role);
		}
		return created;
	}
	
	public void deleteDipendente(long id)throws DAOException, NamingException {
		User u = userBC.getById(id);
		
		userBC = new UserBC();
		userBC.delete(u);
	}
	
	/*-------------------------------dip in base all'id*/
	public User getById(long id) throws DAOException, NamingException {
		
		return userBC.getById(id);
	}
	
	/*-------------------------------dip in base allo username*/
	public User getByUsername(String username) throws DAOException, NamingException {
		return userBC.getByUsername(username);
	}
	
	public User getByEmail(String email) throws DAOException, NamingException {
		return userBC.getByEmail(email);
	}
	
	/*------------------------------------Percentuale completamento progetto in base all'id*/
	public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
		return projectBC.getPercentualeCompletamento(id);
	}
	/*------------------------------------Percentuale completamento progetto in base all'id*/
	public void updatePercentualeCompletamentoProjectID(long idProgetto, int n) throws DAOException, NamingException {
		 Project project = projectBC.getById(idProgetto);
		 project.setPercentualeCompletamento(n);
		 projectBC.createOrUpdate(project);
	}
	
	/*--------------------------------------Progetto in base all'ID*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		return projectBC.getById(id);
	}
	
	// -------------crate or update timesheet
	public void createOrUpdateTimesheet(Timesheet timesheet) throws DAOException, NamingException {
		tBC.createOrUpdate(timesheet);
	}
	
	//-----------------------timesheet by id
	public Timesheet timesheetByID(long id) throws DAOException, NamingException {
		return tBC.getById(id);
	}
	
	//-----------------------timesheet by dip
	public List<Timesheet> getTimesheetByDipendente(long id) throws DAOException, NamingException {
		return tBC.getListByDipendente(id);
	}
	
	//-----------------------delete timesheet 
	public void deleteTimesheet(long id) throws DAOException, NamingException {
		tBC.delete(id);
	}
	
	//------------------Update stato ProjectTask
	public ProjectTask updateProjectTaskStato(StatoTask stato, long id) throws DAOException, NamingException {
		return ptBC.updateStato(stato, id);
	}
	
	//------------------ ProjectTask by id
	public ProjectTask getProjectTaskById(long id) throws DAOException, NamingException {
		return ptBC.getByID(id);
	}
	
	//---------/////////ProjectTask by dipendente
	public List<ProjectTask> getProjectTaskByDipendente(long id) throws DAOException, NamingException {
		return ptBC.getByDipendente(id);
	}
	
	//------------------------- ProjectTask by proj
	public List<ProjectTask> getProjectTaskByProject(long id) throws DAOException, NamingException {
		return ptBC.getByProject(id);
	}
	
	//------------------------get all skill
	public Skill[] getAllSkill() throws DAOException, NamingException {
		return sBC.getByAll();
	}
	
	//----------add skill
	public void addSkill(long id, Skill skill) throws DAOException, NamingException {
		userBC.addSkill(userBC.getById(id), skill);
	}
  	/*--------------------------------Ruoli di un utente*/
	public Role[] getRolesById(long id) throws DAOException, NamingException {
		return userBC.getRolesById(id);
	}
	
	public List<ProjectTask> getTaskByFaseAndProject(Fase fase, long idProject) throws DAOException, NamingException{
		List<ProjectTask> tasks = new ArrayList<ProjectTask>();
		for(ProjectTask task : ptBC.getByProject(idProject)) {
			if(task.getFase() == fase)
				tasks.add(task);
		}
		return tasks;
	}
	
	public Set<Project> getProjectByDipendente(long id) throws DAOException, NamingException {
		Set<Project> projects = new HashSet<Project>();
		List<ProjectTask> tasks = ptBC.getByDipendente(id);
		for(ProjectTask task : tasks) {
			projects.add(projectBC.getById(task.getIdProgetto()));
		}
		return projects;
	}
	
	/*-------------------------------Utenti in base al ruolo*/
	public User[] getAllDipendenti() throws DAOException, NamingException {
		return userBC.getByRole(Ruoli.DIPENDENTE);
	}
	
	//---------------------skill del dipendnete
	public Skill[] getSkillsByUser(long id) throws DAOException, NamingException {
		return sBC.getByUser(id);
	}
	//---------------------skill del dipendnete
	public Skill getSkillsById(long id) throws DAOException, NamingException {
		return sBC.getByID(id);
	}
	public void createOrupdateAuditLog(AuditLog log) throws DAOException, NamingException {
		auditLogBC.createOrUpdate(log);
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

	public Project createOrUpdateProject(Project project) throws DAOException, NamingException {
		projectBC = new ProjectBC();
		return projectBC.createOrUpdate(project);
	}

	
}
