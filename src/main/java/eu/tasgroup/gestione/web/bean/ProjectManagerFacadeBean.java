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
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
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
	private List<ProjectTask> tasks;

	private boolean success;
	private boolean error;

	@Inject
	private UserSessionBean userSessionBean;

	public ProjectManagerFacadeBean() throws DAOException, NamingException {
		pf = ProjectManagerFacade.getInstance();
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
			this.tasks = Arrays.asList(pf.getAllProjectTask());

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
				tasks = Arrays.asList(pf.getAllProjectTask());
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/" + UserSessionBean.getServletContextName()
								+ "/projectManager/pm-task.xhtml?success=true");
			} else {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/" + UserSessionBean.getServletContextName()
								+ "/projectManager/pm-task.xhtml?error=true");
			}
		} catch (NamingException | DAOException e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect("/" + UserSessionBean.getServletContextName()
								+ "/projectManager/pm-task.xhtml?error=true");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
