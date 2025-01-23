package eu.tasgroup.gestione.businesscomponent;

import java.sql.Connection;

import javax.naming.NamingException;

import eu.tasgroup.gestione.architecture.dbaccess.DBAccess;
import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.architetture.dao.PaymentDAO;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class PaymentBC {

	private Connection conn;
	private PaymentDAO paymentDAO;

	public PaymentBC()  {
		
		paymentDAO = PaymentDAO.getFactory();
	}

	public void create(Payment payment) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			paymentDAO.create(conn, payment);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public void delete(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			paymentDAO.delete(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Payment getById(long id) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return paymentDAO.getById(conn, id);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Payment[] getByProject(Project project) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return paymentDAO.getByProject(conn, project);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Payment[] getByUser(User user) throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return paymentDAO.getByUser(conn, user);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}

	public Payment[] getAll() throws DAOException, NamingException {
		try {
			conn = DBAccess.getConnection();
			return paymentDAO.getAll(conn);
		} finally {
			DBAccess.closeConnection(conn);
		}
	}
}
