package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import eu.tasgroup.gestione.businesscomponent.model.Timesheet;

public class TimesheetDAO extends DAOAdapter<Timesheet> implements DAOConstants{
	
	private TimesheetDAO() {}
	
	public static TimesheetDAO getFactory() {
		return new TimesheetDAO();
	}

	@Override
	public void create(Connection conn, Timesheet entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_TIMESHEET);
			
			ps.setLong(1, entity.getIdDipendente());
			ps.setLong(2, entity.getIdProgetto());
			ps.setLong(3, entity.getIdTask());
			ps.setDouble(4, entity.getOreLavorate());
			ps.setDate(5, new java.sql.Date(entity.getData().getTime()));
			
			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void update(Connection conn, Timesheet entity) throws DAOException {
		PreparedStatement ps;
		
		try {
			ps = conn.prepareStatement(UPDATE_TIMESHEET);
			
			ps.setLong(1, entity.getIdDipendente());
			ps.setLong(2, entity.getIdProgetto());
			ps.setLong(3, entity.getIdTask());
			ps.setDouble(4, entity.getOreLavorate());
			ps.setDate(5, new java.sql.Date(entity.getData().getTime()));
			ps.setLong(6, entity.getId());
			
			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	public void approva(Connection conn, long id, boolean stato_approvazione) throws DAOException {
		PreparedStatement ps;
		
		try {
			ps = conn.prepareStatement(APPROVA_TIMESHEET);
			
			ps.setBoolean(1, stato_approvazione);
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
			ps = conn.prepareStatement(DELETE_TIMESHEET);

			ps.setLong(1, id);

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public Timesheet getById(Connection conn, long id) throws DAOException {
		Timesheet timesheet = null;
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(SELECT_TIMESHEET);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				timesheet = new Timesheet(); 
				timesheet.setId(rs.getLong(1));
				timesheet.setIdDipendente(rs.getLong(2));
				timesheet.setIdProgetto(rs.getLong(3));
				timesheet.setIdTask(rs.getLong(4));
				timesheet.setOreLavorate(rs.getDouble(5));
				timesheet.setData(new java.util.Date(rs.getDate(6).getTime()));
				timesheet.setApprovato(rs.getObject(7) != null ? rs.getBoolean(7) : null);

			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return timesheet;
	}

	@Override
	public Timesheet[] getAll(Connection conn) throws DAOException {
		Timesheet[] timesheets = null;
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery(SELECT_TIMESHEETS);
			rs.last();
			timesheets = new Timesheet[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); ++i) {
				Timesheet timesheet = new Timesheet();
				timesheet = new Timesheet(); 
				timesheet.setId(rs.getLong(1));
				timesheet.setIdDipendente(rs.getLong(2));
				timesheet.setIdProgetto(rs.getLong(3));
				timesheet.setIdTask(rs.getLong(4));
				timesheet.setOreLavorate(rs.getDouble(5));
				timesheet.setData(new java.util.Date(rs.getDate(6).getTime()));
				timesheet.setApprovato(rs.getObject(7) != null ? rs.getBoolean(7) : null);
				timesheets[i] = timesheet;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return timesheets;
	}
	
	public List<Timesheet> getByDipendente(Connection conn, long idDipendente) throws DAOException{
		List<Timesheet> timesheets = new ArrayList<Timesheet>();
		PreparedStatement ps;
		
		try {
			ps = conn.prepareStatement(SELECT_TIMESHEET_BY_DIPENDENTE);
			ps.setLong(1, idDipendente);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Timesheet timesheet = new Timesheet();
				timesheet = new Timesheet(); 
				timesheet.setId(rs.getLong(1));
				timesheet.setIdDipendente(rs.getLong(2));
				timesheet.setIdProgetto(rs.getLong(3));
				timesheet.setIdTask(rs.getLong(4));
				timesheet.setOreLavorate(rs.getDouble(5));
				timesheet.setData(new java.util.Date(rs.getDate(6).getTime()));
				timesheet.setApprovato(rs.getObject(7) != null ? rs.getBoolean(7) : null);
				timesheets.add(timesheet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return timesheets;
	}
	
	public List<Timesheet> getByProjectManager(Connection conn, long idMAnager) throws DAOException{
		List<Timesheet> timesheets = new ArrayList<Timesheet>();
		PreparedStatement ps;
		
		try {
			ps = conn.prepareStatement(SELECT_TIMESHEETS_BY_PROJECT_MANAGER);
			ps.setLong(1, idMAnager);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Timesheet timesheet = new Timesheet();
				timesheet = new Timesheet(); 
				timesheet.setId(rs.getLong(1));
				timesheet.setIdDipendente(rs.getLong(2));
				timesheet.setIdProgetto(rs.getLong(3));
				timesheet.setIdTask(rs.getLong(4));
				timesheet.setOreLavorate(rs.getDouble(5));
				timesheet.setData(new java.util.Date(rs.getDate(6).getTime()));
				timesheet.setApprovato(rs.getObject(7) != null ? rs.getBoolean(7) : null);
				timesheets.add(timesheet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return timesheets;
	}
	public List<Timesheet> getByProject(Connection conn, long idDipendente) throws DAOException{
		List<Timesheet> timesheets = new ArrayList<Timesheet>();
		PreparedStatement ps;
		
		try {
			ps = conn.prepareStatement(SELECT_TIMESHEET_BY_PROJECT);
			ps.setLong(1, idDipendente);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				Timesheet timesheet = new Timesheet();
				timesheet = new Timesheet(); 
				timesheet.setId(rs.getLong(1));
				timesheet.setIdDipendente(rs.getLong(2));
				timesheet.setIdProgetto(rs.getLong(3));
				timesheet.setIdTask(rs.getLong(4));
				timesheet.setOreLavorate(rs.getDouble(5));
				timesheet.setData(new java.util.Date(rs.getDate(6).getTime()));
				timesheet.setApprovato(rs.getObject(7) != null ? rs.getBoolean(7) : null);
				timesheets.add(timesheet);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return timesheets;
	}

}
