package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import eu.tasgroup.gestione.businesscomponent.model.AuditLog;

public class AuditLogDAO extends DAOAdapter<AuditLog> implements DAOConstants {
	private AuditLogDAO() {
	}

	public static AuditLogDAO getFactory() {
		return new AuditLogDAO();
	}

	@Override
	public void create(Connection conn, AuditLog entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_AUDIT_LOG);
			ps.setString(1, entity.getUtente());
			ps.setString(2, entity.getOperazione());
			ps.setTimestamp(3, new Timestamp(entity.getData().getTime()));

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void update(Connection conn, AuditLog entity) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(UPDATE_AUDT_LOG);

			ps.setString(1, entity.getUtente());
			ps.setString(2, entity.getOperazione());
			ps.setTimestamp(3, new Timestamp(entity.getData().getTime()));
			ps.setLong(4, entity.getId());

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_AUDIT_LOG);

			ps.setLong(1, id);

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public AuditLog getById(Connection conn, long id) throws DAOException {
		AuditLog auditLog = null;
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(SELECT_AUDIT_LOG);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				auditLog = new AuditLog();
				auditLog.setId(rs.getLong(1));
				auditLog.setUtente(rs.getString(2));
				auditLog.setOperazione(rs.getString(3));
				auditLog.setData(new java.util.Date(rs.getTimestamp(4).getTime()));

			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return auditLog;
	}

	@Override
	public AuditLog[] getAll(Connection conn) throws DAOException {
		AuditLog[] auditLogs = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_AUDIT_LOGS);

			rs.last();
			auditLogs = new AuditLog[rs.getRow()];
			rs.beforeFirst();

			for (int i = 0; rs.next(); ++i) {
				AuditLog auditLog = new AuditLog();
				auditLog.setId(rs.getLong(1));
				auditLog.setUtente(rs.getString(2));
				auditLog.setOperazione(rs.getString(3));
				auditLog.setData(new java.util.Date(rs.getTimestamp(4).getTime()));

				auditLogs[i] = auditLog;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return auditLogs;
	}

}
