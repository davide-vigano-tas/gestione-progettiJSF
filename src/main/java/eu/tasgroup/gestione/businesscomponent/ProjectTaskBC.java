package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;
import java.util.List;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.ProjectTaskDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;

public class ProjectTaskBC {
	private Connection conn;
	private ProjectTaskDAO ptDAO;

	public ProjectTaskBC() throws DAOException, NamingException {
		
		this.ptDAO = ProjectTaskDAO.getFactory();
	}

	public ProjectTask createOrUpdate(ProjectTask pt) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			if (ptDAO.getById(conn, pt.getId()) != null) {
				ptDAO.update(conn, pt);
				return pt;
			} else {
				ptDAO.create(conn, pt);
				ProjectTask[] tasks = ptDAO.getAll(conn);

				ProjectTask newTask = new ProjectTask();
				newTask = tasks[tasks.length - 1];

				for (int i = 0; i < tasks.length - 1; i++) {
					if (tasks[i].getId() > newTask.getId())
						newTask = tasks[i];
				}
				return newTask;
			}
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public ProjectTask updateFase(Fase fase, long id) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			ptDAO.updateFase(conn, fase, id);
			return ptDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public ProjectTask updateStato(StatoTask stato, long id) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			ptDAO.updateStato(conn, stato, id);
			return ptDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public void delete(long id) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			ptDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public ProjectTask getByID(long id) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			return ptDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public ProjectTask[] getAll() throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			return ptDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<ProjectTask> getByDipendente(long idDip) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			return ptDAO.getByDipendente(conn, idDip);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<ProjectTask> getByProject(long idProject) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			return ptDAO.getByProject(conn, idProject);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public List<ProjectTask> getByProjectManager(long idProjectManager) throws DAOException, NamingException {
		try {
			this.conn = DBAccess.getConnection();
			return ptDAO.getByProjectManager(conn, idProjectManager);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
}
