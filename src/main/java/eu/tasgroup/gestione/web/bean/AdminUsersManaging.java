package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import eu.tasgroup.gestione.businesscomponent.model.User;
@Named
@SessionScoped
public class AdminUsersManaging implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6451291615506722321L;
	
	private User userDetails;
	private String roleToAdd;
	private String roleToDelete;
	private List<String> selectedUserRoles;
	public User getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(User userDetails) {
		System.err.println("Setted user details: "+userDetails);
		this.userDetails = userDetails;
	}
	public String getRoleToAdd() {
		return roleToAdd;
	}
	public void setRoleToAdd(String roleToAdd) {
		this.roleToAdd = roleToAdd;
	}
	public String getRoleToDelete() {
		return roleToDelete;
	}
	public void setRoleToDelete(String roleToDelete) {
		this.roleToDelete = roleToDelete;
	}
	public List<String> getSelectedUserRoles() {
		return selectedUserRoles;
	}
	public void setSelectedUserRoles(List<String> selectedUserRoles) {
		this.selectedUserRoles = selectedUserRoles;
	}
	
	

}
