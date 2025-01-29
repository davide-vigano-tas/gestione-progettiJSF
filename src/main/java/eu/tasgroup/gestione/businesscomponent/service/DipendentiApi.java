package eu.tasgroup.gestione.businesscomponent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;

@Path("/dipendentiService")
public class DipendentiApi {
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getDipendenti() throws DAOException, NamingException {
		User[] users = AdminFacade.getInstance().getAllUsers();
		List<User> dip = new ArrayList<User>();
		for(User u : users) {
			Role[] roles = AdminFacade.getInstance().getRolesById(u.getId());
			if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.DIPENDENTE))) {
				dip.add(u);
			}
		}
		return dip;
		
		
	}
	
	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String create(
			@FormParam("nome") String nome, @FormParam("cognome") String cognome,
			@FormParam("username") String username, @FormParam("password") String password,
			@FormParam("email") String email) {
		
		User user = new User();
		user.setNome(nome);
		user.setCognome(cognome);
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		
		try {
			user = AdminFacade.getInstance().createUser(user);
			Role role = new Role();
			role.setIdUser(user.getId());
			role.setRole(Ruoli.DIPENDENTE);
			AdminFacade.getInstance().addRole(user, role);
			return "[{\"success\" : \"user created\"}, "+userJsonConvert(user)+"]";
		} catch (DAOException | NamingException e) {
			return "{\"error\": \"Error during creation\"}";
		}
	}
	
	private String userJsonConvert(User user) {
		return "{\n" + "  \"id\": " + user.getId() + ",\n" + "  \"nome\": \"" + user.getCognome() + "\",\n"
				+ "  \"cognome\": \"" + user.getEmail() + "\",\n" + "  \"username\": \"" + user.getUsername() + "\",\n"
				+ "  \"password\": \"" + user.getPassword() + "\",\n" + "  \"email\": \"" + user.getEmail() + "\",\n"
				+ "  \"tentativiFalliti\": " + user.getTentativiFalliti() + ",\n" + "  \"locked\": " + user.isLocked()
				+ ",\n" + "  \"dataCreazione\": \"" + user.getDataCreazione() + "\"\n" + "}";
	}
	
	
	@PUT
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String update(
			@FormParam("nome") String nome, @FormParam("cognome") String cognome,
			@FormParam("username") String username) {
		try {
			User user = AdminFacade.getInstance().getByUsername(username);
			if(user==null) return "{\"error\" : \"user not found\"}";
			user.setNome(nome);
			user.setCognome(cognome);
			user = AdminFacade.getInstance().createUser(user);
			return "[{\"success\" : \"user updated\"}, "+userJsonConvert(user)+"]";
		} catch (DAOException | NamingException e) {
			return "{\"error\": \"Error during update\"}";
		}
	}

	
	@DELETE
	@Path("/delete")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String delete(
			@FormParam("id") String id) {
		try {
			User user = AdminFacade.getInstance().getUserById(Long.parseLong(id));
			if(user==null) return "{\"error\" : \"user not found\"}";
			AdminFacade.getInstance().deleteUser(user);
			return "{\"success\" : \"user deleted\"}";
		} catch (DAOException | NamingException e) {
			return "{\"error\": \"Error during update\"}";
		}
	}

}
