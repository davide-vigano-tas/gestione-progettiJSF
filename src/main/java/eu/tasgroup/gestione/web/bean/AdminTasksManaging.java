package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.Timesheet;
@Named
@SessionScoped
public class AdminTasksManaging implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2112360149526720903L;

	
	private ProjectTask task;
	private List<Timesheet> timesheets;
	public ProjectTask getTask() {
		return task;
	}
	public void setTask(ProjectTask task) {
		this.task = task;
	}
	public List<Timesheet> getTimesheets() {
		return timesheets;
	}
	public void setTimesheets(List<Timesheet> timesheets) {
		this.timesheets = timesheets;
	}
	
	
	

}
