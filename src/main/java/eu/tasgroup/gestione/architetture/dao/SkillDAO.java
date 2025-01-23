package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.model.Skill;

public class SkillDAO extends DAOAdapter<Skill> implements DAOConstants {

	private SkillDAO() {
	}

	public static SkillDAO getFactory() {
		return new SkillDAO();
	}

	@Override
	public void create(Connection conn, Skill entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_SKILL);
			ps.setString(1, entity.getTipo().name());

			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public Skill getById(Connection conn, long id) throws DAOException {
		Skill skill = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_SKILL);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				skill = new Skill();
				skill.setId(rs.getLong(1));
				skill.setTipo(Skills.valueOf(rs.getString(2)));

			}

			return skill;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public Skill[] getAll(Connection conn) throws DAOException {
		Skill[] skills = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_SKILLS);
			rs.last();
			skills = new Skill[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				Skill skill = new Skill();
				skill.setId(rs.getLong(1));
				skill.setTipo(Skills.valueOf(rs.getString(2)));
				skills[i] = skill;
			}
			return skills;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public Skill[] getByTipo(Connection conn, Skills skilltype) throws DAOException {
		Skill[] skills = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_SKILLS_BY_TIPO, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setString(1, skilltype.name());
			ResultSet rs = ps.executeQuery();
			rs.last();
			skills = new Skill[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				Skill skill = new Skill();
				skill.setId(rs.getLong(1));
				skill.setTipo(Skills.valueOf(rs.getString(2)));
				skills[i] = skill;

			}

			return skills;
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

	public Skill[] getByUser(Connection conn, long user) throws DAOException {
		Skill[] skills = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_SKILLS_BY_USER, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setLong(1, user);
			ResultSet rs = ps.executeQuery();
			rs.last();
			skills = new Skill[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				Skill skill = new Skill();
				skill.setId(rs.getLong(1));
				skill.setTipo(Skills.valueOf(rs.getString(2)));
				skills[i] = skill;

			}

			return skills;
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_SKILL);
			ps.setLong(1, id);
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public void deleteByTipo(Connection conn, Skills skill) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_SKILLS_BY_TIPO);
			ps.setString(1, skill.name());
			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

}
