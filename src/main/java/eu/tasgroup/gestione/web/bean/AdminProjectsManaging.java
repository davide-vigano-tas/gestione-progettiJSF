package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eu.tasgroup.gestione.businesscomponent.model.Project;
@Named
@SessionScoped
public class AdminProjectsManaging implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8535107733466933816L;

	
	private Project project;
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}

	

}
