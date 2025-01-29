package eu.tasgroup.gestione.businesscomponent.service;

import java.util.Arrays;

import javax.naming.NamingException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;
import eu.tasgroup.gestione.businesscomponent.security.JwtUtil;

@Path("/login")
public class LoginJWTService {
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@FormParam("username") String username, @FormParam("password") String password) throws DAOException, NamingException {
		User user = AdminFacade.getInstance().getByUsername(username);
		if(user == null) return "{ \"error\": \"user not found\"}";
		Role[] roles = AdminFacade.getInstance().getRolesById(user.getId());
		if(Arrays.asList(roles).stream().anyMatch(r -> r.getRole().equals(Ruoli.ADMIN))) {
			
            // Verifica la password
            if (!Algoritmo.verificaPassword(password, user.getPassword())) {
                // Incrementa i tentativi falliti
                user.setTentativiFalliti(user.getTentativiFalliti() + 1);

                // Blocca l'account se necessario
                if (user.getTentativiFalliti() >= 5) {
                    user.setLocked(true);
                }

                // Aggiorna l'utente nel database
                AdminFacade.getInstance().createUser(user);

                return "{\"error\": \"wrong credentials\"}";
            } else {
                if (user.getTentativiFalliti() > 0) {
                    user.setTentativiFalliti(0);
                    AdminFacade.getInstance().createUser(user);
                }

                // Genera il token JWT
                JwtUtil jwtUtil = new JwtUtil();
                String token = jwtUtil.generateToken(username);
                
                return "{\"token\" : \""+token+"\"}";
            }
		} else return "{\"error\": \"forbidden\"}";
	}

}
