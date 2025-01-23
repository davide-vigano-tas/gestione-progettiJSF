package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.AuditLogDAO;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;

public class AuditLogBC {
	private Connection conn;
	private AuditLogDAO alDAO;
	
	public AuditLogBC() throws DAOException, NamingException {
		alDAO = AuditLogDAO.getFactory();
	}
	
	public void createOrUpdate(AuditLog al) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			if(al.getId() == 0)
				alDAO.create(conn, al);
			else
				alDAO.update(conn, al);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public void delete(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			alDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public AuditLog getById(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return alDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	public AuditLog[] getAll() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return alDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
	
	

}
