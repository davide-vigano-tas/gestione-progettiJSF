package eu.tasgroup.gestione.web.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.Skill;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;
@Named
@ViewScoped
public class AdminFacadeBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -171999536902730384L;
	private AdminFacade af;
	private User logged;
	private String optionValue;
	private String errorMessage;
	private List<User> users;
	private List<Skill> skills;
	private String skillToAdd;
	private List<String> skillsList;
	private List<AuditLog> logs;
	private List<Project> projects;
	private List<Timesheet> timesheets;
	private List<Ticket> tickets;
	private List<Payment> payments;
	
	private String barModel;

	public String getSkillToAdd() {
		return skillToAdd;
	}


	public void setSkillToAdd(String skillToAdd) {
		this.skillToAdd = skillToAdd;
	}


	public List<Skill> getSkills() {
		return skills;
	}


	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

	@Inject
	private UserSessionBean userSessionBean;
	
	@Inject
	private AdminUsersManaging adminUsersManaging;
	
	@Inject
	private AdminProjectsManaging adminProjectsManaging;
	
	@Inject
	private AdminTasksManaging adminTasksManaging;
	
	
	public AdminUsersManaging getAdminUsersManaging() {
		return adminUsersManaging;
	}


	public void setAdminUsersManaging(AdminUsersManaging adminUsersManaging) {
		this.adminUsersManaging = adminUsersManaging;
	}


	@PostConstruct
	public void init() {
		try {
			 FacesContext facesContext = FacesContext.getCurrentInstance();
			this.logged = getByUsername(userSessionBean.getUsername());
		    String option = facesContext.getExternalContext().getRequestParameterMap().get("option");
	        if (option != null) {
	            optionValue = option;
	        }
	        users = Arrays.asList(getAllUsers());
	        String skill = facesContext.getExternalContext().getRequestParameterMap().get("skill");
	        if(skill != null) {
	        	users = Arrays.asList(getDipendentiBySkill(getSkillById(Long.parseLong(skill))));
	        }
	      
	        skillsList = new ArrayList<String>();
	        for(Skills sk : Skills.values()) {
	        	skillsList.add(sk.name());
	        }
	        skills = Arrays.asList(getAllSkills());
	        logs = Arrays.asList(getAllAuditLogs());
	        projects = Arrays.asList(getAllProjects());
	        
	        timesheets = Arrays.asList(getAllTimesheet());
	        tickets = Arrays.asList(getAllTicket());
	        
	        payments = Arrays.asList(getAllPayments());
	        String error = facesContext.getExternalContext().getRequestParameterMap().get("error");
	        if ("username_taken".equals(error)) {
	            errorMessage = "Username già in uso";
	        } else if ("email_taken".equals(error)) {
	            errorMessage = "Email già in uso";
	        }
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
		}
	}
	



	public AdminFacadeBean() throws DAOException, NamingException {
		af = AdminFacade.getInstance();
	}
	
	public void setNotAssignedEmployees() throws DAOException, NamingException {
		users = Arrays.asList(getDipendentiNonAssegnati());
	}
	
	public String getOptionValue() {
		return optionValue;
	}
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}
	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}
	public User getLogged() {
		return logged;
	}
	public void setLogged(User logged) {
		this.logged = logged;
	}
	public void createUser(User user, String role) throws IOException, DAOException, NamingException {
	 
			user = af.createUser(user);
			Role ruolo = new Role();
			ruolo.setIdUser(user.getId());
			ruolo.setRole(Ruoli.valueOf(role));
			af.addRole(user, ruolo);
			AuditLog log = new AuditLog();
			log.setData(new Date());
			log.setOperazione("Aggiunto "+role+" "+user.getUsername());
			log.setUtente(userSessionBean.getUsername());
			af.createOrupdateAuditLog(log);
	        // Redirect to another page
	        FacesContext.getCurrentInstance().getExternalContext()
	        .redirect("/"+UserSessionBean.getServletContextName()+"/admin/users.xhtml");
	   
	}
	
	public String getUserDetails(long id) throws DAOException, NamingException {
		this.adminUsersManaging.setUserDetails(getUserById(id));
		Role[] roles = getRolesById(id);
		List<String> selectedUserRoles = new ArrayList<String>();
		for(Role r : roles) {
			selectedUserRoles.add(r.getRole().name());
		}
		this.adminUsersManaging.setSelectedUserRoles(selectedUserRoles);
		System.err.println("id :"+id);
		return "user-details";
	}

	
	public void toggleLockUser() throws DAOException, NamingException {
		User userDetails = adminUsersManaging.getUserDetails();
		userDetails.setLocked(!userDetails.isLocked());
	}
	/*------------------------------------Tutti gli utenti*/
	public User[] getAllUsers() throws DAOException, NamingException {
		return af.getAllUsers();
	}
	

	/*------------------------------------Utente in base all'id*/
	public User getUserById(long id) throws DAOException, NamingException {
		return af.getUserById(id);
	}
	
	/*-------------------------------Utente in base allo username*/
	public User getByUsername(String username) throws DAOException, NamingException {
		return af.getByUsername(username);
	}
	/*-------------------------------Utente in base alla mail*/
	public User getByEmail(String email) throws DAOException, NamingException {
		return af.getByEmail(email);
	}
	
	/*-------------------------------Dipendenti in base alla skill*/
	public User[] getDipendentiBySkill(Skill skill) throws DAOException, NamingException {
		return af.getDipendentiBySkill(skill);
	}
	
	
	public String showDipendentiBySkill(Skill skill) throws DAOException, NamingException {
		System.err.println("Chiamato show skill: "+skill);
		  users =Arrays.asList(getDipendentiBySkill(skill));
		  return "users?skill="+skill.getId()+"&faces-redirect=true";
		
	}
	
	/*-------------------------------Utenti in base al ruolo*/
	public User[] getUsersByRole(Ruoli ruolo) throws DAOException, NamingException {
		return af.getUsersByRole(ruolo);
	}
	/*--------------------------- lista dei dipendenti non assegnati */
	public User[] getDipendentiNonAssegnati() throws DAOException, NamingException {
		return af.getDipendentiNonAssegnati();
	}

	
	/*-------------------------------Aggiunge una skill al dipendente*/
	public void addSkill(User user, Skill skill) throws DAOException, NamingException {
		af.addSkill(user, skill);
	}
	

	/*-------------------------------Aggiunge un ruolo all'utente*/
	public void addRole(User user, Role role) throws DAOException, NamingException {
		af.addRole(user, role);
	}
	
	public void addRoleToCurrent() throws DAOException, NamingException {

	
				
				User userDetails = adminUsersManaging.getUserDetails();
				Role ruolo = new Role();
				ruolo.setIdUser(userDetails.getId());
				ruolo.setRole(Ruoli.valueOf(adminUsersManaging.getRoleToAdd()));
				af.addRole(userDetails, ruolo);
				AuditLog log = new AuditLog();
				log.setData(new Date());
				log.setOperazione("Aggiunto ruolo : "+Ruoli.valueOf(adminUsersManaging.getRoleToDelete()).name()+","
						+ " a: "+userDetails.getUsername());
				log.setUtente(userSessionBean.getUsername());
				af.createOrupdateAuditLog(log);
				Role[] roles = getRolesById(userDetails.getId());
				List<String> selectedUserRoles = new ArrayList<String>();
				for(Role r : roles) {
					selectedUserRoles.add(r.getRole().name());
				}
				adminUsersManaging.setSelectedUserRoles(selectedUserRoles);
	}
	
	/*-------------------------------Modifica un ruolo dell'utente*/
	public void updateRole(Role from, Ruoli to) throws DAOException, NamingException {
		af.updateRole(from, to);
	}
	
	/*--------------------------------Ruoli di un utente*/
	public Role[] getRolesByUsername(String username) throws DAOException, NamingException {
		return af.getRolesByUsername(username);
	}
	
	public String getRolesString(String username) throws DAOException, NamingException {
		String roles = "";
		for(Role r: getRolesByUsername(username)) {
			roles += r.getRole().name()+" ";

		}
	
		return roles;
	}
	/*--------------------------------Ruoli di un utente*/
	public Role[] getRolesById(long id) throws DAOException, NamingException {
		return af.getRolesById(id);
	}
	
	/*--------------------------------Elimina il ruolo associato all'utente*/
	public void deleteRole(Ruoli role, User user) throws DAOException, NamingException {
		af.deleteRole(role, user);
	}
	public void deleteRoleFromCurrent() throws DAOException, NamingException {
		User userDetails = adminUsersManaging.getUserDetails();
		af.deleteRole(Ruoli.valueOf(adminUsersManaging.getRoleToDelete()), userDetails);
		AuditLog log = new AuditLog();
		log.setData(new Date());
		log.setOperazione("Eliminato ruolo : "+Ruoli.valueOf(adminUsersManaging.getRoleToDelete()).name()+","
				+ " da: "+userDetails.getUsername());
		log.setUtente(userSessionBean.getUsername());
		af.createOrupdateAuditLog(log);
		Role[] roles = getRolesById(userDetails.getId());
		List<String> selectedUserRoles = new ArrayList<String>();
		for(Role r : roles) {
			selectedUserRoles.add(r.getRole().name());
		}
		adminUsersManaging.setSelectedUserRoles(selectedUserRoles);
	}
	
	/*----------------------------------Elimina utente*/
	public void deleteUser(User user) throws DAOException, NamingException {
		 af.deleteUser(user);
	}
	
	
	/*--------------------------------------Elenco timesheet*/
	public Timesheet[] getAllTimesheet() throws DAOException, NamingException {
		return af.getAllTimesheet();
	}
	
	/*--------------------------------------Timesheet in base all'id*/
	public Timesheet getTimesheetById(long id) throws DAOException, NamingException {
		return af.getTimesheetById(id);
	}
	
	/*--------------------------------------Elenco timesheet assiocati a un dipendente*/
	public List<Timesheet> getTimesheetsByDipendente(long id) throws DAOException, NamingException {
		return af.getTimesheetsByDipendente(id);
	}
	
	/*--------------------------------------Elenco timesheet assiocati a un progetto*/
	public List<Timesheet> getTimesheetsByProject(long id) throws DAOException, NamingException {
		return af.getTimesheetsByProject(id);
	}
	
	/*--------------------------------------Creazione skill*/
	public Skill createSkill(Skill skill) throws DAOException, NamingException {
		return af.createSkill(skill);
	}
	public String addNewSkill() throws DAOException, NamingException {
		Skill nuova = new Skill();
		Skill[] present = af.getSkillsByTipo(Skills.valueOf(skillToAdd));
		if(present.length == 0) {
			nuova.setTipo(Skills.valueOf(skillToAdd));
			af.createSkill(nuova);
			AuditLog log = new AuditLog();
			log.setData(new Date());
			log.setOperazione("Aggiunta skill : "+Skills.valueOf(skillToAdd));
			log.setUtente(userSessionBean.getUsername());
			af.createOrupdateAuditLog(log);
		}
		return "skills?faces-redirect=true";
	}
	/*-------------------------------------- skill in base all'id*/
	public Skill getSkillById(long id) throws DAOException, NamingException {
		return af.getSkillById(id);
	}
	/*--------------------------------------Elenco skill*/
	public Skill[] getAllSkills() throws DAOException, NamingException {
		return af.getAllSkills();
	}
	/*--------------------------------------Elenco skill in base al tipo*/
	public Skill[] getSkillsByTipo(Skills skill) throws DAOException, NamingException {
		return af.getSkillsByTipo(skill);
	}
	/*--------------------------------------Elenco skill in base al dipendente*/
	public Skill[] getSkillsByDipendente(User user) throws DAOException, NamingException {
		return af.getSkillsByDipendente(user);
	}
	
	/*---------------------------------Elimina skill*/
	public void deleteSkill(Skill skill) throws DAOException, NamingException {
		af.deleteSkill(skill);
	}
	/*---------------------------------Elimina skill*/
	public void deleteSkillByTipo(Skills skill) throws DAOException, NamingException {
		af.deleteSkillByTipo(skill);
	}
	/*----------------------------------Task in base all'id*/
	public ProjectTask getTaskById(long id) throws DAOException, NamingException {
		return af.getTaskById(id);
	}
	public String getTaskDetails(long id) throws DAOException, NamingException {
		this.adminTasksManaging.setTask(getTaskById(id));
		Timesheet[] timesheets = af.getAllTimesheet();
		List<Timesheet> listTimesheets = new ArrayList<Timesheet>();
		for(Timesheet t : timesheets) {
			if(t.getIdTask() == id) {
				listTimesheets.add(t);
			}
		}
		this.adminTasksManaging.setTimesheets(listTimesheets);
		return "task-details";
	}
	
	/*-----------------------------------Elenco task in base al progetto*/
	public List<ProjectTask> getTasksByProject(Project project) throws DAOException, NamingException {
		return af.getTasksByProject(project);
	}
	
	/*-----------------------------------Elenco task in base al dipendente*/
	public List<ProjectTask> getTasksByDipendente(User user) throws DAOException, NamingException {
		return af.getTasksByDipendente(user);
	}
	/*-----------------------------------Crea o modifca il projetto*/
	public Project createOrUpdateProject(Project project) throws DAOException, NamingException {
		return af.createOrUpdateProject(project);
	}
	
	/*---------------------------Elimina progetto*/
	public void deleteProject(Project project) throws DAOException, NamingException {
		af.deleteProject(project);
	}
	
	/*--------------------------Percentuale completamento progetto*/
	public int getPercentualeCompletamento(Project project) throws DAOException, NamingException {
		return af.getPercentualeCompletamento(project);
	}
	
	/*--------------------------------Elenco progetto*/
	public Project[] getAllProjects() throws DAOException, NamingException {
		return af.getAllProjects();
	}
	
	/*--------------------------------Progetto in base all'id*/
	public Project getProjectById(long id) throws DAOException, NamingException {
		return af.getProjectById(id);
	}
	
	public String getProjectDetails(long id) throws DAOException, NamingException {
		this.adminProjectsManaging.setProject(getProjectById(id));
		return "project-details";
	}
	/*--------------------------------Elenco progetti in base allo stato*/
	public List<Project> getProjectByStatus(StatoProgetto stato) throws DAOException, NamingException {
		return af.getProjectByStatus(stato);
	}
	
	/*--------------------------------Elenco progetti in base al cliente*/
	public List<Project> getProjectByCliente(User cliente) throws DAOException, NamingException {
		return af.getProjectByCliente(cliente);
	}
	
	/*--------------------------------Elenco progetti in base allo stato*/
	public List<Project> getProjectByResp(User res) throws DAOException, NamingException {
		return af.getProjectByResp(res);
	}
	
	/*-----------------------------Elenco pagamenti*/
	public Payment[] getAllPayments() throws DAOException, NamingException {
		return af.getAllPayments();
	}
	/*-----------------------------Pagamento in base all'id*/
	public Payment getPaymentById(long id) throws DAOException, NamingException {
		return af.getPaymentById(id);
	}
	
	/*-----------------------------Elimina pagamento*/
	public void deletePayment(Payment payment) throws DAOException, NamingException {
	    af.deletePayment(payment);
	}
	
	/*-----------------------------Elenco pagamenti di un progetto*/
	public Payment[] getPaymentsByProject(Project project) throws DAOException, NamingException {
		return af.getPaymentsByProject(project);
	}
	/*-----------------------------Elenco pagamenti di un cliente*/
	public Payment[] getPaymentsByCliente(User cliente) throws DAOException, NamingException {
		return af.getPaymentsByCliente(cliente);
	}
	/*-----------------------------Elenco auditlog*/
	public AuditLog[] getAllAuditLogs() throws DAOException, NamingException {
		return af.getAllAuditLogs();
	}
	
	/*-------------------------------Update Auditlog*/
	public void createOrupdateAuditLog(AuditLog log) throws DAOException, NamingException {
		af.createOrupdateAuditLog(log);
	}
	
	/*-------------------------------Delete auditLog*/
	public String deleteAuditLog(AuditLog log) throws DAOException, NamingException {
		af.deleteAuditLog(log);
		return "auditlogs?faces-redirect=true";
	}
	
	public AuditLog getAuditLogById(long id) throws DAOException, NamingException {
		return af.getAuditLogById(id);
	}
	
	/*------------------------------Ticket*/
	
	public Ticket[] getAllTicket() throws DAOException, NamingException {
		return af.getAllTicket();
	}
	
	public Ticket getTicketById(long id) throws DAOException, NamingException {
		return af.getTicketById(id);
	}
	
	public Ticket[] getAllOpen() throws DAOException, NamingException {
		return af.getAllOpen();
	}
	
	public Ticket[] getAllClosed() throws DAOException, NamingException {
		return af.getAllClosed();
	}
	
	public Ticket[] getByDipendente(long id) throws DAOException, NamingException {
		return af.getByDipendente(id);
	}
	
	public void closeTicket(long id) throws DAOException, NamingException {
		af.closeTicket(id);
	}
	
	public void deleteTicket(long id) throws DAOException, NamingException {
		af.deleteTicket(id);
	}


	public List<String> getSkillsList() {
		return skillsList;
	}


	public void setSkillsList(List<String> skillsList) {
		this.skillsList = skillsList;
	}


	public List<AuditLog> getLogs() {
		return logs;
	}


	public void setLogs(List<AuditLog> logs) {
		this.logs = logs;
	}


	public List<Project> getProjects() {
		return projects;
	}


	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}


	public AdminProjectsManaging getAdminProjectsManaging() {
		return adminProjectsManaging;
	}


	public void setAdminProjectsManaging(AdminProjectsManaging adminProjectsManaging) {
		this.adminProjectsManaging = adminProjectsManaging;
	}


	public AdminTasksManaging getAdminTasksManaging() {
		return adminTasksManaging;
	}


	public void setAdminTasksManaging(AdminTasksManaging adminTasksManaging) {
		this.adminTasksManaging = adminTasksManaging;
	}


	public String getBarModel() {
		return barModel;
	}


	public void setBarModel(String barModel) {
		this.barModel = barModel;
	}


	public List<Timesheet> getTimesheets() {
		return timesheets;
	}


	public void setTimesheets(List<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}


	public List<Ticket> getTickets() {
		return tickets;
	}


	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}


	public List<Payment> getPayments() {
		return payments;
	}


	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}










	
}

