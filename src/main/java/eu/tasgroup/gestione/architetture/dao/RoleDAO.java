package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.Role;

public class RoleDAO extends DAOAdapter<Role> implements DAOConstants {

	private RoleDAO() {
	}

	public static RoleDAO getFactory() {
		return new RoleDAO();
	}

	@Override
	public void create(Connection conn, Role entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_USERS_ROLES);
			ps.setLong(1, entity.getIdUser());
			ps.setString(2, entity.getRole().name());

			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public void update(Connection conn, Role entity, Ruoli ruolo) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(UPDATE_USER_ROLES);
			ps.setString(1, ruolo.name());
			ps.setLong(2, entity.getIdUser());
			ps.setString(3, entity.getRole().name());
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public Ruoli[] getByUserId(Connection conn, long id_user) throws DAOException {
		Ruoli[] roles = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_USER_ROLES_BY_USER_ID, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setLong(1, id_user);
			ResultSet rs = ps.executeQuery();
			rs.last();
			roles = new Ruoli[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {

				roles[i] = Ruoli.valueOf(rs.getString(1));

			}

			return roles;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public Ruoli[] getByUsername(Connection conn, String username) throws DAOException {
		Ruoli[] roles = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_USER_ROLES_BY_USERNAME, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.last();
			roles = new Ruoli[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {

				roles[i] = Ruoli.valueOf(rs.getString(1));

			}

			return roles;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public void delete(Connection conn, Ruoli ruolo, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_USER_ROLE);
			ps.setString(1, ruolo.name());
			ps.setLong(2, id);
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

}
