package eu.tasgroup.gestione.web.bean;

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
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.facade.DipendenteFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;

@Named
@RequestScoped
public class DipendenteFacadeBean {
	
	private DipendenteFacade df;
	private User user;
	private Timesheet timesheet;
	private long idSkill;
	private Project project;
	private List<ProjectTask> tasks;
	private List<Project> dip_projects;
	private List<Timesheet> timesheets;
	private List<Skill> dip_skills;
	private List<Skill> general_skills;
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
			this.general_skills = getAllSkills();
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
	
	public List<Skill> getAllSkills() throws DAOException, NamingException{
		System.out.println(Arrays.asList(df.getAllSkill()));
		return Arrays.asList(df.getAllSkill());
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
		
		return "dip-timesheets?faces-redirect=true";
		
	}
	
	public String addSkill(long idSkill ,long idDipendente) throws DAOException, NamingException {
		
		
		
			Skill[] skills = df.getSkillsByUser(idDipendente);
			Skill skill = df.getSkillsById(idSkill);
			if(!Arrays.asList(skills).stream().anyMatch( s -> s.getTipo().equals(skill.getTipo()))) {
				Skill s = df.getSkillsById(idSkill);
				df.addSkill(idDipendente, s);
				AuditLog log = new AuditLog();
				log.setData(new Date());
				log.setOperazione("Aggiunta skill : "+s.getTipo().name() + " ad utente: " + df.getById(idDipendente).getEmail());
				log.setUtente(df.getById(idDipendente).getEmail());
				df.createOrupdateAuditLog(log);
			}
		
			return "dip-skills?faces-redirect=true";
	}
	
	public String updateStatoTask(long taskId, String statoTask, String username) throws DAOException, NamingException {
		Long id = taskId;
		StatoTask stato = StatoTask.valueOf(statoTask.toUpperCase());

		if (stato.equals(StatoTask.DA_INIZIARE)) {
			stato = StatoTask.IN_PROGRESS;
			
			Project project = df.getProjectById(df.getProjectTaskById(id).getIdProgetto());
			if(project.getStato() == StatoProgetto.CREATO) {
				project.setStato(StatoProgetto.IN_PROGRESS);
				df.createOrUpdateProject(project);
			}
			
			df.updateProjectTaskStato(stato, id);
		}else if (stato.equals(StatoTask.IN_PROGRESS)) {
			stato = StatoTask.COMPLETATO;
			df.updateProjectTaskStato(stato, id);
			
			// calcolo percentuale progetto
			ProjectTask task = df.getProjectTaskById(id);
			List<ProjectTask> tasks;
			double percentuale = 0;
			double percentualeParziale;
			// Ciclo su tutte le fasi
			for (Fase fase : Fase.values()) {
				double percentualeFase =0;
				// grupppo di task appartenenti alla determinata fase
				tasks = df.getTaskByFaseAndProject(fase, task.getIdProgetto());

				// controllo che ci siano task della fase corrente
				if (tasks.size() != 0) {
					// se la fase è deploy o plan valse 10 sulla percentuale totale
					// 10+20+20+20+20+10
					if (fase != Fase.PLAN && fase != Fase.DEPLOY) {
						percentualeParziale = 20 / tasks.size();
						for (ProjectTask el : tasks) {
							if (el.getStato() == StatoTask.COMPLETATO) {
								percentuale += percentualeParziale;
								percentualeFase += percentualeParziale;
							}
						}
						if(percentualeFase>=20) {
				        	String emailContent = "<!DOCTYPE html><html lang=\"en\">"
				                    + "<head><meta charset=\"UTF-8\"></head>"
				                    + "<body>"
				                    + "<div style='background-color:#f4f4f4;padding:20px;'>"
				                    + "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
				                    + "<h1 style='text-align:center;color:#007bff;'>Fase: "+fase+" completata</h1>"
				                    + "</div></div></body></html>";
				        	//EmailUtil.sendEmail(df.getById(df.getProjectById(task.getIdProgetto()).getIdResponsabile()).getEmail(), "Fase completata", emailContent);
						}
					} else {
						percentualeParziale = 10 / tasks.size();
						for (ProjectTask el : tasks) {
							if (el.getStato() == StatoTask.COMPLETATO)
								percentuale += percentualeParziale;
							percentualeFase += percentualeParziale;
						}
						if(percentualeFase>=10) {
				        	String emailContent = "<!DOCTYPE html><html lang=\"en\">"
				                    + "<head><meta charset=\"UTF-8\"></head>"
				                    + "<body>"
				                    + "<div style='background-color:#f4f4f4;padding:20px;'>"
				                    + "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
				                    + "<h1 style='text-align:center;color:#007bff;'>Fase: "+fase+" completata</h1>"
				                    + "</div></div></body></html>";
				        	//EmailUtil.sendEmail(df.getById(df.getProjectById(task.getIdProgetto()).getIdResponsabile()).getEmail(), "Fase completata", emailContent);
						}
					}
				}
			}
			if(percentuale>=100) {
				percentuale=100;

				Project project = df.getProjectById(df.getProjectTaskById(id).getIdProgetto());
				project.setStato(StatoProgetto.COMPLETATO);
				df.createOrUpdateProject(project);
			}
			
			int value= (int) percentuale;
			df.updatePercentualeCompletamentoProjectID(task.getIdProgetto(), value);

		}
		AuditLog log = new AuditLog();
		log.setData(new Date());
		log.setOperazione("Stato task aggiornanato a : "+stato + " da utente: " +username);
		log.setUtente(username);
		df.createOrupdateAuditLog(log);
		
		return "dip-tasks?faces-redirect=true";
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

	public long getIdSkill() {
		return idSkill;
	}

	public void setIdSkill(long idSkill) {
		this.idSkill = idSkill;
	}

	public List<Skill> getGeneral_skills() {
		return general_skills;
	}

	public void setGeneral_skills(List<Skill> general_skills) {
		this.general_skills = general_skills;
	}


	
	
	

	
}
