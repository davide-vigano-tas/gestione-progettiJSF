package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.model.User;
import eu.tasgroup.gestione.businesscomponent.security.Algoritmo;

public class UserDAO extends DAOAdapter<User> implements DAOConstants {

	private UserDAO() {
	}

	public static UserDAO getFactory() {
		return new UserDAO();
	}

	@Override
	public void create(Connection conn, User entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_USERS);
			ps.setString(1, entity.getNome());
			ps.setString(2, entity.getCognome());
			ps.setString(3, entity.getUsername());
			ps.setString(4, Algoritmo.converti(entity.getPassword()));
			ps.setString(5, entity.getEmail());

			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void update(Connection conn, User entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(UPDATE_USER);
			ps.setString(1, entity.getNome());
			ps.setString(2, entity.getCognome());
			ps.setString(3, entity.getUsername());
			ps.setString(4, entity.getEmail());
			ps.setInt(5, entity.getTentativiFalliti());
			ps.setBoolean(6, entity.isLocked());
			ps.setLong(7, entity.getId());
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public User getById(Connection conn, long id) throws DAOException {
		User user = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_USER_ID);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setCognome(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setTentativiFalliti(rs.getInt(7));
				user.setLocked(rs.getBoolean(8));
				user.setDataCreazione(rs.getTimestamp(9).toLocalDateTime());

			}

			return user;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public User getByUsername(Connection conn, String username) throws DAOException {
		User user = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_USER_USERNAME);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setCognome(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setTentativiFalliti(rs.getInt(7));
				user.setLocked(rs.getBoolean(8));
				user.setDataCreazione(rs.getTimestamp(9).toLocalDateTime());

			}

			return user;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public User getByEmail(Connection conn, String email) throws DAOException {
		User user = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_USER_EMAIL);
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				user = new User();
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setCognome(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setTentativiFalliti(rs.getInt(7));
				user.setLocked(rs.getBoolean(8));
				user.setDataCreazione(rs.getTimestamp(9).toLocalDateTime());

			}

			return user;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public User[] getAll(Connection conn) throws DAOException {
		User[] users = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_USERS);
			rs.last();
			users = new User[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setCognome(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setTentativiFalliti(rs.getInt(7));
				user.setLocked(rs.getBoolean(8));
				user.setDataCreazione(rs.getTimestamp(9).toLocalDateTime());
				users[i] = user;
			}
			return users;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	public User[] getDipendentiNonAssegnati(Connection conn) throws DAOException {
		User[] users = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_DIPENDENTI_NON_ASSEGNATI);
			rs.last();
			users = new User[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setCognome(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setTentativiFalliti(rs.getInt(7));
				user.setLocked(rs.getBoolean(8));
				user.setDataCreazione(rs.getTimestamp(9).toLocalDateTime());
				users[i] = user;
			}
			return users;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public User[] getBySkill(Connection conn, Skills skill) throws DAOException {
		User[] users = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_USERS_BY_SKILL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, skill.name());
			ResultSet rs = ps.executeQuery();
			rs.last();
			users = new User[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setCognome(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setTentativiFalliti(rs.getInt(7));
				user.setLocked(rs.getBoolean(8));
				user.setDataCreazione(rs.getTimestamp(9).toLocalDateTime());

				users[i] = user;

			}

			return users;
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}
	public User[] getByRole(Connection conn, Ruoli role) throws DAOException {
		User[] users = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_USERS_BY_ROLES, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, role.name());
			ResultSet rs = ps.executeQuery();
			rs.last();
			users = new User[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				User user = new User();
				user.setId(rs.getLong(1));
				user.setNome(rs.getString(2));
				user.setCognome(rs.getString(3));
				user.setUsername(rs.getString(4));
				user.setPassword(rs.getString(5));
				user.setEmail(rs.getString(6));
				user.setTentativiFalliti(rs.getInt(7));
				user.setLocked(rs.getBoolean(8));
				user.setDataCreazione(rs.getTimestamp(9).toLocalDateTime());
				
				users[i] = user;
				
			}
			
			return users;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		
	}
	
	

	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_USER);
			ps.setLong(1, id);
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
}
