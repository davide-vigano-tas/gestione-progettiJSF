package eu.tasgroup.gestione.web.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;

@Named
@RequestScoped
public class CSVTasksBean  implements Serializable{


	private static final long serialVersionUID = 2447587280983871905L;
	
	@Inject
	private UserSessionBean userSessionBean;
	
	public void downloadCSVTasks(long id) throws DAOException, NamingException {
		  FacesContext facesContext = FacesContext.getCurrentInstance();
	        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

	        String fileName = "data.csv";
	        response.setContentType("text/csv");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

	        try (OutputStream outputStream = response.getOutputStream()) {
	            String csvData = generateCsvContent(id);
	            outputStream.write(csvData.getBytes());
	            outputStream.flush();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            facesContext.responseComplete();
	        }
	}
	
    private String generateCsvContent(long id) throws DAOException, NamingException {
        StringBuilder sb = new StringBuilder();
       sb.append("ID;Nome;Descrizione;Dipendente;Stato;Scadenza;Fase\n");
    	
    	
    		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyy");
    		Project p = AdminFacade.getInstance().getProjectById(id);
    		List<ProjectTask> tasks = AdminFacade.getInstance().getTasksByProject(p);
    		for(ProjectTask task: tasks) {
    			sb.append(task.getId()+";"+task.getNomeTask()+";"+task.getDescrizione()+
    					";"+task.getIdDipendente()+";"+task.getStato()+";"+formato.format(task.getScadenza())+
    					";"+task.getFase()+"\n");
    		}
    	
    		AuditLog log = new AuditLog();
    		log.setData(new Date());
    		log.setOperazione("Stampa CSV task");
    		log.setUtente(userSessionBean.getUsername());
    		AdminFacade.getInstance().createOrupdateAuditLog(log);
   
        return sb.toString();
    }

	public UserSessionBean getUserSessionBean() {
		return userSessionBean;
	}

	public void setUserSessionBean(UserSessionBean userSessionBean) {
		this.userSessionBean = userSessionBean;
	}

}
