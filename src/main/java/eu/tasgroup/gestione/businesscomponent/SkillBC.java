package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.SkillDAO;
import eu.tasgroup.gestione.businesscomponent.enumerated.Skills;
import eu.tasgroup.gestione.businesscomponent.model.Skill;

public class SkillBC {
	private Connection conn;
	private SkillDAO sDAO;

	public SkillBC() {
		
		this.sDAO = SkillDAO.getFactory();
	}

	public Skill create(Skill s) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			sDAO.create(conn, s);
			Skill[] skills = sDAO.getAll(conn);

			Skill newSkill = new Skill();
			newSkill = skills[skills.length - 1];

			for (int i = 0; i < skills.length - 1; i++) {
				if (skills[i].getId() > newSkill.getId())
					newSkill = skills[i];
			}
			return newSkill;
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public Skill getByID(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return sDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public Skill[] getByAll() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return sDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public Skill[] getByTipo(Skills tipo) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return sDAO.getByTipo(conn, tipo);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public Skill[] getByUser(long idUser) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return sDAO.getByUser(conn, idUser);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public void delete(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			sDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public void deleteByTipo(Skills tipo) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			sDAO.deleteByTipo(conn, tipo);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

}
