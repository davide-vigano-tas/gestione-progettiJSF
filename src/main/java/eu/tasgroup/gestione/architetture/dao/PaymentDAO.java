package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class PaymentDAO extends DAOAdapter<Payment> implements DAOConstants {
	
	private PaymentDAO() {}
	
	public static PaymentDAO getFactory() {
		return new PaymentDAO();
	}

	@Override
	public void create(Connection conn, Payment entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_PAYMENT);
			ps.setLong(1, entity.getIdProgetto());
			ps.setDouble(2, entity.getCifra());

			ps.execute();
			

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}


	

	@Override
	public Payment getById(Connection conn, long id) throws DAOException {
		Payment payment = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_PAYMENT_BY_ID);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				payment = new Payment();
				payment.setId(rs.getLong(1));
				payment.setIdProgetto(rs.getLong(2));
				payment.setCifra(rs.getDouble(3));
				
			} 
			
			return payment;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	



	@Override
	public Payment[] getAll(Connection conn) throws DAOException {
		Payment[] payments = null;

		try {
			Statement stmt = conn.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			
			ResultSet rs = stmt.executeQuery(SELECT_PAYMENTS);
			rs.last();
			payments = new Payment[rs.getRow()];
			rs.beforeFirst();
			for(int i = 0; rs.next(); i++) {
				Payment payment = new Payment();
				payment.setId(rs.getLong(1));
				payment.setIdProgetto(rs.getLong(2));
				payment.setCifra(rs.getDouble(3));
				payments[i] = payment;
			} 
			return payments;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	public Payment[] getByUser(Connection conn, User user) throws DAOException {
		Payment[] payments = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_PAYMENTS_BY_USER,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setLong(1, user.getId());
			ResultSet rs = ps.executeQuery();
			rs.last();
			payments = new Payment[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				Payment payment = new Payment();
				payment.setId(rs.getLong(1));
				payment.setIdProgetto(rs.getLong(2));
				payment.setCifra(rs.getDouble(3));
				payments[i] = payment;
				
				
			} 
			
			return payments;
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}
	
	public Payment[] getByProject(Connection conn, Project project) throws DAOException {
		Payment[] payments = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_PAYMENTS_BY_PROJECT,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setLong(1, project.getId());
			ResultSet rs = ps.executeQuery();
			rs.last();
			payments = new Payment[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				Payment payment = new Payment();
				payment.setId(rs.getLong(1));
				payment.setIdProgetto(rs.getLong(2));
				payment.setCifra(rs.getDouble(3));
				payments[i] = payment;
				
				
			} 
			
			return payments;
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}
	
	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_PAYMENT);
			ps.setLong(1, id);
			ps.execute();
			

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
}
