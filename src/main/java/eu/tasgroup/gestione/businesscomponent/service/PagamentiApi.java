package eu.tasgroup.gestione.businesscomponent.service;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Payment;

@Path("/pagamentiService")
public class PagamentiApi {
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Payment> getPagamenti() throws DAOException, NamingException {
		return Arrays.asList(AdminFacade.getInstance().getAllPayments());
		
		
	}

}
