package eu.tasgroup.gestione.web.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.EscapeHTML;
@Named
@RequestScoped
public class DipendenteFacadeBean {
	
	private DipendenteFacade df;
	private User user;
	private Timesheet timesheet;
	private Project project;
	private List<ProjectTask> tasks;
	private List<Project> dip_projects;
	private List<Timesheet> timesheets;
	private List<Skill> dip_skills;
	private List<Ticket> dip_tickets;
	
	
	@Inject
	private UserSessionBean userSessionBean;
	
	public DipendenteFacadeBean() throws DAOException, NamingException {
		df = DipendenteFacade.getInstance();
	}
	
	@PostConstruct
	public void init() {
		try {
			this.user = getByUsername(userSessionBean.getUsername());
			this.tasks = getProjectTaskByDipendente(user.getId());
			this.timesheets = getTimesheetsByDipendente(user.getId());
			this.dip_skills = getSkillsByDipendente(user.getId());
			this.dip_projects = getProjectByDipendente(user.getId());
			//this.dip_tickets = getTicketsByDipendente(user.getId());
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
		}
	}
	
	public User getByUsername(String username) throws DAOException, NamingException {
		return df.getByUsername(username);
	}
	
	public List<ProjectTask> getProjectTaskByDipendente(long id) throws DAOException, NamingException{
		return df.getProjectTaskByDipendente(id);
	}
	
	public List<Project> getProjectByDipendente(long id) throws DAOException, NamingException{
		return new ArrayList<>(df.getProjectByDipendente(id));
	}
	
	public List<Timesheet> getTimesheetsByDipendente(long id) throws DAOException, NamingException{
		return df.getTimesheetByDipendente(id);
	}
	
	public List<Skill> getSkillsByDipendente(long id) throws DAOException, NamingException{
		List<Skill> skills = Arrays.asList(df.getSkillsByUser(id));
		return skills;
	}
	
	public List<Ticket> getTicketsByDipendente(long id) throws DAOException, NamingException{
		List<Ticket> tickets = Arrays.asList(df.getByDipendente(id));
		return tickets;
	}
	//------------------------------------------------------------
	
	public String createTimesheet(Timesheet timesheet,long idDipendente) throws DAOException, NamingException {
		
		//SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		
		Long idTask = timesheet.getIdTask();
		Long idProgetto = df.getProjectTaskById(idTask).getIdProgetto();
		Double ore = timesheet.getOreLavorate();
		Date data = timesheet.getData();
		
		Timesheet timesheets = new Timesheet();
		timesheets.setIdDipendente(idDipendente);
		timesheets.setIdProgetto(idProgetto);
		timesheets.setIdTask(idTask);
		timesheets.setOreLavorate(ore);
		timesheets.setData(data);
		
		System.out.println(timesheets);
		
		df.createOrUpdateTimesheet(timesheets);
		
		return "dipendente/dip-timesheets.xhtml";
		
	}
	
	//-----------------------------------------------------------------------------------------------
	public String getProjectNameByIdProject(long id) throws DAOException, NamingException{
		return df.getProjectById(id).getNomeProgetto();
	}
	
	public String getEmailByIdDipendente(long id) throws DAOException, NamingException{
		return df.getById(id).getEmail();
	}
	
	public String getTaskNameByIdTask(long id) throws DAOException, NamingException{
		return df.getProjectTaskById(id).getNomeTask();
	}

	public DipendenteFacade getDf() {
		return df;
	}

	public void setDf(DipendenteFacade df) {
		this.df = df;
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

	public List<ProjectTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ProjectTask> tasks) {
		this.tasks = tasks;
	}



	public List<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(List<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	

	public List<Skill> getDip_skills() {
		return dip_skills;
	}

	public void setDip_skills(List<Skill> dip_skills) {
		this.dip_skills = dip_skills;
	}

	public List<Ticket> getDip_tickets() {
		return dip_tickets;
	}

	public void setDip_tickets(List<Ticket> dip_tickets) {
		this.dip_tickets = dip_tickets;
	}

	public List<Project> getDip_projects() {
		return dip_projects;
	}

	public void setDip_projects(List<Project> dip_projects) {
		this.dip_projects = dip_projects;
	}

	public Timesheet getTimesheet() {
		return timesheet;
	}

	public void setTimesheet(Timesheet timesheet) {
		this.timesheet = timesheet;
	}


	
	
	

	
}
