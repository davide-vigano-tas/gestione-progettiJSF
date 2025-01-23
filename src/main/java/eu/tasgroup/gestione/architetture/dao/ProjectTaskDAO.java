package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import eu.tasgroup.gestione.businesscomponent.enumerated.Fase;
import eu.tasgroup.gestione.businesscomponent.enumerated.StatoTask;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;

public class ProjectTaskDAO extends DAOAdapter<ProjectTask> implements DAOConstants {

	private ProjectTaskDAO() {
	}

	public static ProjectTaskDAO getFactory() {
		return new ProjectTaskDAO();
	}

	@Override
	public void create(Connection conn, ProjectTask entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_PROJECT_TASK);

			ps.setLong(1, entity.getIdProgetto());
			ps.setString(2, entity.getNomeTask());
			ps.setString(3, entity.getDescrizione());
			ps.setLong(4, entity.getIdDipendente());
			ps.setDate(5, new java.sql.Date(entity.getScadenza().getTime()));
			ps.setString(6, entity.getFase().name());

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void update(Connection conn, ProjectTask entity) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(UPDATE_PROJECT_TASK);

			ps.setString(1, entity.getNomeTask());
			ps.setString(2, entity.getDescrizione());
			ps.setLong(3, entity.getIdDipendente());
			ps.setString(4, entity.getStato().name());
			ps.setDate(5, new java.sql.Date(entity.getScadenza().getTime()));
			ps.setString(6, entity.getFase().name());
			ps.setLong(7, entity.getId());

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public void updateDipendente(Connection conn, ProjectTask entity) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(UPDATE_PROJECT_TASK_DIPENDENTE);

			ps.setLong(1, entity.getIdDipendente());
			ps.setLong(2, entity.getId());

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public void updateFase(Connection conn, Fase fase, long id) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(UPDATE_PROJECT_TASK_FASE);

			ps.setString(1, fase.name());
			ps.setLong(2, id);

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public void updateStato(Connection conn, StatoTask stato, long id) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(UPDATE_PROJECT_TASK_STATO);

			ps.setString(1, stato.name());
			ps.setLong(2, id);

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_PROJECT_TASK);

			ps.setLong(1, id);

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public ProjectTask getById(Connection conn, long id) throws DAOException {
		ProjectTask projectTask = null;
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(SELECT_PROJECT_TASK);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				projectTask = new ProjectTask();
				projectTask.setId(rs.getLong(1));
				projectTask.setIdProgetto(rs.getLong(2));
				projectTask.setNomeTask(rs.getString(3));
				projectTask.setDescrizione(rs.getString(4));
				projectTask.setIdDipendente(rs.getLong(5));
				projectTask.setStato(StatoTask.valueOf(rs.getString(6)));
				projectTask.setScadenza(new java.util.Date(rs.getDate(7).getTime()));
				projectTask.setFase(Fase.valueOf(rs.getString(8)));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return projectTask;
	}

	@Override
	public ProjectTask[] getAll(Connection conn) throws DAOException {
		ProjectTask[] projectTasks = null;
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(SELECT_PROJECT_TASKS);
			rs.last();
			projectTasks = new ProjectTask[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); ++i) {
				ProjectTask projectTask = new ProjectTask();
				projectTask.setId(rs.getLong(1));
				projectTask.setIdProgetto(rs.getLong(2));
				projectTask.setNomeTask(rs.getString(3));
				projectTask.setDescrizione(rs.getString(4));
				projectTask.setIdDipendente(rs.getLong(5));
				projectTask.setStato(StatoTask.valueOf(rs.getString(6)));
				projectTask.setScadenza(new java.util.Date(rs.getDate(7).getTime()));
				projectTask.setFase(Fase.valueOf(rs.getString(8)));
				projectTasks[i] = projectTask;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return projectTasks;

	}

	public List<ProjectTask> getByDipendente(Connection conn, long idDipendente) throws DAOException {
		List<ProjectTask> projectTasks = new ArrayList<ProjectTask>();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_PROJECT_TASKS_BY_DIPENDENTE);
			ps.setLong(1, idDipendente);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProjectTask projectTask = new ProjectTask();
				projectTask.setId(rs.getLong(1));
				projectTask.setIdProgetto(rs.getLong(2));
				projectTask.setNomeTask(rs.getString(3));
				projectTask.setDescrizione(rs.getString(4));
				projectTask.setIdDipendente(rs.getLong(5));
				projectTask.setStato(StatoTask.valueOf(rs.getString(6)));
				projectTask.setScadenza(new java.util.Date(rs.getDate(7).getTime()));
				projectTask.setFase(Fase.valueOf(rs.getString(8)));
				projectTasks.add(projectTask);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return projectTasks;
	}

	public List<ProjectTask> getByProject(Connection conn, long idProject) throws DAOException {
		List<ProjectTask> projectTasks = new ArrayList<ProjectTask>();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_PROJECT_TASKS_BY_PROJECT);
			ps.setLong(1, idProject);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProjectTask projectTask = new ProjectTask();
				projectTask.setId(rs.getLong(1));
				projectTask.setIdProgetto(rs.getLong(2));
				projectTask.setNomeTask(rs.getString(3));
				projectTask.setDescrizione(rs.getString(4));
				projectTask.setIdDipendente(rs.getLong(5));
				projectTask.setStato(StatoTask.valueOf(rs.getString(6)));
				projectTask.setScadenza(new java.util.Date(rs.getDate(7).getTime()));
				projectTask.setFase(Fase.valueOf(rs.getString(8)));
				projectTasks.add(projectTask);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return projectTasks;
	}

	public List<ProjectTask> getByProjectManager(Connection conn, long idProjectManager) throws DAOException {
		List<ProjectTask> projectTasks = new ArrayList<ProjectTask>();
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_PROJECT_TASKS_BY_PROJECT_MANAGER);
			ps.setLong(1, idProjectManager);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProjectTask projectTask = new ProjectTask();
				projectTask.setId(rs.getLong(1));
				projectTask.setIdProgetto(rs.getLong(2));
				projectTask.setNomeTask(rs.getString(3));
				projectTask.setDescrizione(rs.getString(4));
				projectTask.setIdDipendente(rs.getLong(5));
				projectTask.setStato(StatoTask.valueOf(rs.getString(6)));
				projectTask.setScadenza(new java.util.Date(rs.getDate(7).getTime()));
				projectTask.setFase(Fase.valueOf(rs.getString(8)));
				projectTasks.add(projectTask);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return projectTasks;
	}

}
