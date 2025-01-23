package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import eu.tasgroup.gestione.businesscomponent.model.Ticket;

public class TicketDAO extends DAOAdapter<Ticket> implements DAOConstants {
	private TicketDAO() {
	}

	public static TicketDAO getFactory() {
		return new TicketDAO();
	}

	@Override
	public void create(Connection conn, Ticket entity) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(INSERT_TICKET);
			ps.setLong(1, entity.getOpener());
			ps.setString(2, entity.getTitle());
			ps.setString(3, entity.getDescription());
			ps.setTimestamp(4, new Timestamp(entity.getCreated_at().getTime()));
			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void update(Connection conn, Ticket entity) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(UPDATE_TICKET);

			ps.setString(1, entity.getTitle());
			ps.setString(2, entity.getDescription());
			ps.setLong(3, entity.getId());

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_TICKET);

			ps.setLong(1, id);

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public Ticket getById(Connection conn, long id) throws DAOException {
		Ticket ticket = null;
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(SELECT_TICKET);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ticket.setId(rs.getLong(1));
				ticket.setOpener(rs.getLong(2));
				ticket.setTitle(rs.getString(3));
				ticket.setDescription(rs.getString(4));
				ticket.setCreated_at(new Date(rs.getTimestamp(5).getTime()));
				Timestamp closedAt = rs.getTimestamp(6);
				if(closedAt != null) {
					ticket.setClosed_at(new Date(closedAt.getTime()));
				}

			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return ticket;
	}
	
	public void closeTicket(Connection conn, long id, Date closedDate) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(CLOSE_TICKET);
			ps.setTimestamp(1, new Timestamp(closedDate.getTime()));
			ps.setLong(2, id);
		
			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public Ticket[] getAll(Connection conn) throws DAOException {
		Ticket[] tickets = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_TICKETS);

			rs.last();
			tickets = new Ticket[rs.getRow()];
			rs.beforeFirst();

			for (int i = 0; rs.next(); ++i) {
				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong(1));
				ticket.setOpener(rs.getLong(2));
				ticket.setTitle(rs.getString(3));
				ticket.setDescription(rs.getString(4));
				ticket.setCreated_at(new Date(rs.getTimestamp(5).getTime()));
				Timestamp closedAt = rs.getTimestamp(6);
				if(closedAt != null) {
					ticket.setClosed_at(new Date(closedAt.getTime()));
				}

				tickets[i] = ticket;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return tickets;
	}	
	
	public Ticket[] getAllOpen(Connection conn) throws DAOException {
		Ticket[] tickets = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_OPEN);

			rs.last();
			tickets = new Ticket[rs.getRow()];
			rs.beforeFirst();

			for (int i = 0; rs.next(); ++i) {
				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong(1));
				ticket.setOpener(rs.getLong(2));
				ticket.setTitle(rs.getString(3));
				ticket.setDescription(rs.getString(4));
				ticket.setCreated_at(new Date(rs.getTimestamp(5).getTime()));
				

				tickets[i] = ticket;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return tickets;
	}	
	
	public Ticket[] getAllClosed(Connection conn) throws DAOException {
		Ticket[] tickets = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_CLOSED);

			rs.last();
			tickets = new Ticket[rs.getRow()];
			rs.beforeFirst();

			for (int i = 0; rs.next(); ++i) {
				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong(1));
				ticket.setOpener(rs.getLong(2));
				ticket.setTitle(rs.getString(3));
				ticket.setDescription(rs.getString(4));
				ticket.setCreated_at(new Date(rs.getTimestamp(5).getTime()));
				Timestamp closedAt = rs.getTimestamp(6);
				if(closedAt != null) {
					ticket.setClosed_at(new Date(closedAt.getTime()));
				}

				tickets[i] = ticket;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return tickets;
	}	
	public Ticket[] getByDipendente(Connection conn, long id) throws DAOException {
		Ticket[] tickets = null;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(SELECT_TICKETS_BY_DIP, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			rs.last();
			tickets = new Ticket[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				Ticket ticket = new Ticket();
				ticket.setId(rs.getLong(1));
				ticket.setOpener(rs.getLong(2));
				ticket.setTitle(rs.getString(3));
				ticket.setDescription(rs.getString(4));
				ticket.setCreated_at(new Date(rs.getTimestamp(5).getTime()));
				Timestamp closedAt = rs.getTimestamp(6);
				if(closedAt != null) {
					ticket.setClosed_at(new Date(closedAt.getTime()));
				}

				tickets[i] = ticket;

			}

			return tickets;
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	
	}

}
