package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.ProjectDAO;
import eu.tasgroup.gestione.architetture.dao.UserDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.utility.EmailUtil;

public class ProjectBC {
	private Connection conn;
	private ProjectDAO projectDAO;
	private UserDAO userDAO;

	public ProjectBC() throws DAOException, NamingException {
		userDAO = UserDAO.getFactory();
		projectDAO = ProjectDAO.getFactory();
	}

	public Project createOrUpdate(Project project) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			if (projectDAO.getById(conn, project.getId()) != null) {
				projectDAO.update(conn, project);
				String emailContent = "<!DOCTYPE html><html lang=\"en\">" + "<head><meta charset=\"UTF-8\"></head>"
						+ "<body>" + "<div style='background-color:#f4f4f4;padding:20px;'>"
						+ "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
						+ "<h1 style='text-align:center;'>Progetto " + project.getNomeProgetto() + " Aggiornato</h1>"
						+ "<p style='text-align:center;'>Descrizione:</p>" + "<h4 style='text-align:center;'>"
						+ project.getDescrizione() + "</h4>"
						+ "<p style='text-align:center;'>Percentuale completamento:</p>"
						+ "<h4 style='text-align:center;'>" + project.getPercentualeCompletamento() + "%" + "</h4>"
						+ "</div></div></body></html>";
				User resp = userDAO.getById(conn, project.getIdResponsabile());
				EmailUtil.sendEmail(resp.getEmail(), "Progetto aggiornato", emailContent);
				return projectDAO.getById(conn, project.getId());
			} else {
				projectDAO.create(conn, project);

				// Restituisco elemento appena creato
				Project newProject = new Project();
				Project[] projects = projectDAO.getAll(conn);

				newProject = projects[projects.length - 1];

				for (int i = 0; i < projects.length - 1; i++) {
					if (projects[i].getId() > newProject.getId())
						newProject = projects[i];
				}
				String emailContent = "<!DOCTYPE html><html lang=\"en\">" + "<head><meta charset=\"UTF-8\"></head>"
						+ "<body>" + "<div style='background-color:#f4f4f4;padding:20px;'>"
						+ "<div style='max-width:600px;margin:0 auto;background:#ffffff;padding:20px;border-radius:8px;'>"
						+ "<h1 style='text-align:center;'>Progetto " + project.getNomeProgetto() + " creato</h1>"
						+ "<p style='text-align:center;'>Descrizione:</p>" + "<h4 style='text-align:center;'>"
						+ project.getDescrizione() + "</h4>" + "</div></div></body></html>";
				User resp = userDAO.getById(conn, project.getIdResponsabile());
				EmailUtil.sendEmail(resp.getEmail(), "Creazione progetto", emailContent);

				return newProject;
			}

		} catch (MessagingException e) {
			e.printStackTrace();
			throw new DAOException(new SQLException(e.getMessage()));
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public void delete(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			projectDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public int getPercentualeCompletamento(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return projectDAO.getCompletamentoByProjectID(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Project getById(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return projectDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Project[] getAll() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return projectDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<Project> getListProjectByStatus(StatoProgetto stato) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return projectDAO.getListProjectByStatus(conn, stato);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<Project> getListProjectByCliente(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return projectDAO.getListProjectByCliente(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<Project> getListProjectByResponsabile(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return projectDAO.getListProjectByResponsabile(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

}
