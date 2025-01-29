package eu.tasgroup.gestione.businesscomponent.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
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

}
