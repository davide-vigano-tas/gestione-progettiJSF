package eu.tasgroup.gestione.web.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.facade.ProjectManagerFacade;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
import eu.tasgroup.gestione.businesscomponent.model.User;

@Named
@SessionScoped
public class ProjectManagerFacadeBean implements Serializable {

	private static final long serialVersionUID = 6382180422910233040L;

	private ProjectManagerFacade pf;
	private User user;
	private Project project;
	private Payment payment;
	private ProjectTask task;
	private List<Project> projects;
	private List<User> clienti;
	private List<User> dipendenti;
	private List<ProjectTask> tasks;
	private List<String> fasi = new ArrayList<String>();
	private List<Timesheet> timesheets;

	private boolean success;
	private boolean error;

	@Inject
	private UserSessionBean userSessionBean;

	public ProjectManagerFacadeBean() throws DAOException, NamingException {
		pf = ProjectManagerFacade.getInstance();

		fasi.add(Fase.PLAN.name());
		fasi.add(Fase.ANALISI.name());
		fasi.add(Fase.DESIGN.name());
		fasi.add(Fase.BUILD.name());
		fasi.add(Fase.TEST.name());
		fasi.add(Fase.DEPLOY.name());
	}

	@PostConstruct
	public void init() {
		try {
			this.user = pf.getProjectManagerByUsername(userSessionBean.getUsername());
			this.projects = pf.getProjectsByResponsabile(user);
			this.payment = new Payment();
			this.project = new Project();
			this.task = new ProjectTask();
			this.clienti = Arrays.asList(pf.getByRole(Ruoli.CLIENTE));
			this.dipendenti = Arrays.asList(pf.getByRole(Ruoli.DIPENDENTE));
			this.tasks = Arrays.asList(pf.getAllProjectTask());
			this.timesheets = Arrays.asList(pf.getAllTimesheet());

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

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<User> getClienti() {
		return clienti;
	}

	public void setClienti(List<User> clienti) {
		this.clienti = clienti;
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

	public ProjectTask getTask() {
		return task;
	}

	public void setTask(ProjectTask task) {
		this.task = task;
	}

	public List<ProjectTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ProjectTask> tasks) {
		this.tasks = tasks;
	}

	public List<User> getDipendenti() {
		return dipendenti;
	}

	public void setDipendenti(List<User> dipendenti) {
		this.dipendenti = dipendenti;
	}

	public List<String> getFasi() {
		return fasi;
	}

	public void setFasi(List<String> fasi) {
		this.fasi = fasi;
	}

	public List<Timesheet> getTimesheets() {
		return timesheets;
	}

	public void setTimesheets(List<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}

	public void createProject() {
		try {
			System.out.println("Progetto: " + project);
			if (project != null) {
				project.setIdResponsabile(user.getId());
				pf.createOrUpdateProject(project);
				projects = pf.getProjectsByResponsabile(user);
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/" + UserSessionBean.getServletContextName()
								+ "/projectManager/projectManager-home.xhtml?success=true");
			} else {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/" + UserSessionBean.getServletContextName()
								+ "/projectManager/projectManager-home.xhtml?error=true");
			}
		} catch (NamingException | DAOException e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/" + UserSessionBean.getServletContextName()
								+ "/projectManager/projectManager-home.xhtml?error=true");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayMessage() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (success) {
			success = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Progetto Creato",
					"Il progetto è stato registrato con successo."));
		}
		if (error) {
			error = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore",
					"Errore durante la generazione del progetto"));
		}
	}

	public void displayMessageTask() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (success) {
			success = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Task Creata",
					"la Task è stato creata con successo."));
		}
		if (error) {
			error = false;
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Errore",
					"Errore durante la generazione della Task"));
		}
	}

	public void createTask() {
		try {
			if (task != null) {
				pf.createOrUpdateProjectTask(task);

				// Aggiorno stato progetto
				Project p = pf.getProjectById(task.getIdProgetto());
				p.setStato(StatoProgetto.IN_PROGRESS);
				pf.createOrUpdateProject(p);
				projects = pf.getProjectsByResponsabile(user);

				// Aggiorno le task
				tasks = Arrays.asList(pf.getAllProjectTask());

				FacesContext.getCurrentInstance().getExternalContext().redirect(
						"/" + UserSessionBean.getServletContextName() + "/projectManager/pm-task.xhtml?success=true");
			} else {
				FacesContext.getCurrentInstance().getExternalContext().redirect(
						"/" + UserSessionBean.getServletContextName() + "/projectManager/pm-task.xhtml?error=true");
			}
		} catch (NamingException | DAOException e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect(
						"/" + UserSessionBean.getServletContextName() + "/projectManager/pm-task.xhtml?error=true");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getDipendenteByID(long id) {
		try {
			return pf.getProjectManagerById(id).getUsername();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getProjectByID(long id) {
		try {
			return pf.getProjectById(id).getNomeProgetto();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getTaskByID(long id) {
		try {
			return pf.getProjectTaskByID(id).getNomeTask();
		} catch (DAOException | NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void approva(Long id) {
		System.out.println("Accettato " + id);
		try {
			pf.approvaTimesheet(id, true);
			this.timesheets = Arrays.asList(pf.getAllTimesheet()); // Aggiorna la lista
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Timesheet approvato"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rifiuta(Long id) {
		System.out.println("Rifiutato " + id);
		try {
			pf.approvaTimesheet(id, false);
			this.timesheets = Arrays.asList(pf.getAllTimesheet()); // Aggiorna la lista
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Successo", "Timesheet rifiutato"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
