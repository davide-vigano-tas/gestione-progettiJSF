package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import eu.tasgroup.gestione.businesscomponent.model.UserSkill;

public class UserSkillDAO extends DAOAdapter<UserSkill> implements DAOConstants {

	private UserSkillDAO() {
	}

	public static UserSkillDAO getFactory() {
		return new UserSkillDAO();
	}

	@Override
	public void create(Connection conn, UserSkill entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_USER_SKILL);
			ps.setLong(1, entity.getIdCompetenze());
			ps.setLong(2, entity.getIdUtente());

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_USER_SKILL);
			ps.setLong(1, id);
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public UserSkill[] getAll(Connection conn) throws DAOException {
		UserSkill[] userSkills = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_USERS_SKILLS);

			rs.last();
			userSkills = new UserSkill[rs.getRow()];
			rs.beforeFirst();

			for (int i = 0; rs.next(); i++) {
				UserSkill userSkill = new UserSkill();

				userSkill.setId(rs.getLong(1));
				userSkill.setIdCompetenze(rs.getLong(2));
				userSkill.setIdUtente(rs.getLong(3));

				userSkills[i] = userSkill;
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return userSkills;
	}

	@Override
	public UserSkill getById(Connection conn, long id) throws DAOException {
		UserSkill userSkill = null;
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(SELECT_USER_SKILL);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				userSkill = new UserSkill();

				userSkill.setId(rs.getLong(1));
				userSkill.setIdCompetenze(rs.getLong(2));
				userSkill.setIdUtente(rs.getLong(3));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return userSkill;
	}

}
