package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;
import java.util.Date;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Ticket;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.utility.EmailUtil;

@Named
@ViewScoped
public class CloseTicketBean implements Serializable{

	private static final long serialVersionUID = -4332250289996851632L;
	
	private String spiegazione;
	
	@Inject
	private UserSessionBean userSessionBean;

	public String getSpiegazione() {
		return spiegazione;
	}

	public void setSpiegazione(String spiegazione) {
		this.spiegazione = spiegazione;
	}
	
	public String close(long id) throws DAOException, NamingException, MessagingException {
		Ticket ticket = AdminFacade.getInstance().getTicketById(id);
		User utente = AdminFacade.getInstance().getUserById(ticket.getOpener());
		AdminFacade.getInstance().closeTicket(ticket.getId());
		
    	String emailContent = "<!DOCTYPE html><html lang=\"en\">"
                + "<head><meta charset=\"UTF-8\"></head>"
                + "<body>"
                + "<div style='background-color:#f4f4f4;padding:20px;'>"
                + "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
                + "<h1 style='text-align:center;color:#007bff;'>Ticket "+ticket.getId()+" chiuso</h1>"
                + "<p style='text-align:center;'>Spiegazione:</p>"
                + "<h4 style='text-align:center;'>"
                + spiegazione
                + "</h4>"
                + "</div></div></body></html>";
    	EmailUtil.sendEmail(utente.getEmail(), "Chiusura ticket", emailContent);
    	AuditLog log = new AuditLog();
    	log.setData(new Date());
    	log.setOperazione("Chiusura ticket di aperto da "+utente.getEmail());
    	log.setUtente(userSessionBean.getUsername());
    	AdminFacade.getInstance().createOrupdateAuditLog(log);
    	
    	return "tickets?faces-redirect=true";
	}

	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

}
