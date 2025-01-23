package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import eu.tasgroup.gestione.businesscomponent.enumerated.StatoProgetto;
import eu.tasgroup.gestione.businesscomponent.model.Project;

public class ProjectDAO implements DAOConstants, GenericDAO<Project> {

	private ProjectDAO() {
	}

	public static ProjectDAO getFactory() {
		return new ProjectDAO();
	}

	@Override
	public void create(Connection conn, Project entity) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(INSERT_PROJECT);
			ps.setString(1, entity.getNomeProgetto());
			ps.setString(2, entity.getDescrizione());
			ps.setDate(3, new Date(entity.getDataInizio().getTime()));
			ps.setDate(4, new Date(entity.getDataFine().getTime()));
			ps.setDouble(5, entity.getBudget());
			ps.setLong(6, entity.getIdCliente());
			ps.setLong(7, entity.getIdResponsabile());
			ps.setDouble(8, entity.getCostoProgetto());

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

	@Override
	public void update(Connection conn, Project entity) throws DAOException {
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(UPDATE_PROJECT);

			ps.setString(1, entity.getNomeProgetto());
			ps.setString(2, entity.getDescrizione());
			ps.setDate(3, new Date(entity.getDataFine().getTime()));
			ps.setDouble(4, entity.getBudget());
			ps.setString(5, entity.getStato().toString());
			ps.setLong(6, entity.getIdResponsabile());
			ps.setLong(7, entity.getPercentualeCompletamento());

			ps.setLong(8, entity.getId());

			ps.execute();

		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void delete(Connection conn, long id) throws DAOException {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(DELETE_PROJECT);
			ps.setLong(1, id);

			ps.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public Project getById(Connection conn, long id) throws DAOException {
		Project progetto = null;
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(SELECT_PROJECT_ID);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				progetto = new Project();
				progetto.setId(rs.getLong(1));
				progetto.setNomeProgetto(rs.getString(2));
				progetto.setDescrizione(rs.getString(3));
				progetto.setDataInizio(new java.util.Date(rs.getDate(4).getTime()));
				progetto.setDataFine(new java.util.Date(rs.getDate(5).getTime()));
				progetto.setBudget(rs.getDouble(6));
				progetto.setStato(StatoProgetto.valueOf(rs.getString(7)));
				progetto.setIdCliente(rs.getLong(8));
				progetto.setIdResponsabile(rs.getLong(9));
				progetto.setPercentualeCompletamento(rs.getInt(10));
				progetto.setCostoProgetto(rs.getDouble(11));
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return progetto;
	}

	public int getCompletamentoByProjectID(Connection conn, long id) throws DAOException {
		int completamento = 0;
		PreparedStatement ps;

		try {
			ps = conn.prepareStatement(SELECT_PROJECT_COMPLETAMENTO);
			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				completamento = rs.getInt(1);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return completamento;
	}

	@Override
	public Project[] getAll(Connection conn) throws DAOException {
		Project[] progetti = null;

		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery(SELECT_PROJECTS);

			rs.last();
			progetti = new Project[rs.getRow()];
			rs.beforeFirst();

			for (int i = 0; rs.next(); i++) {
				Project progetto = new Project();
				progetto.setId(rs.getLong(1));
				progetto.setNomeProgetto(rs.getString(2));
				progetto.setDescrizione(rs.getString(3));
				progetto.setDataInizio(new java.util.Date(rs.getDate(4).getTime()));
				progetto.setDataFine(new java.util.Date(rs.getDate(5).getTime()));
				progetto.setBudget(rs.getDouble(6));
				progetto.setStato(StatoProgetto.valueOf(rs.getString(7)));
				progetto.setIdCliente(rs.getLong(8));
				progetto.setIdResponsabile(rs.getLong(9));
				progetto.setPercentualeCompletamento(rs.getInt(10));
				progetto.setCostoProgetto(rs.getDouble(11));

				progetti[i] = progetto;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return progetti;
	}

	public List<Project> getListProjectByStatus(Connection conn, StatoProgetto stato) throws DAOException {
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT_PROJECT_STATUS);
			ps.setString(1, stato.toString());
			return getListaProgetti(ps);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public List<Project> getListProjectByCliente(Connection conn, long id) throws DAOException {
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT_PROJECTS_CLIENTE);
			ps.setLong(1, id);
			return getListaProgetti(ps);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public List<Project> getListProjectByResponsabile(Connection conn, long id) throws DAOException {
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT_PROJECTS_RESPONSABILE);
			ps.setLong(1, id);
			return getListaProgetti(ps);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	private List<Project> getListaProgetti(PreparedStatement ps) throws SQLException {
		List<Project> progetti = new ArrayList<Project>();
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Project progetto = new Project();
			progetto.setId(rs.getLong(1));
			progetto.setNomeProgetto(rs.getString(2));
			progetto.setDescrizione(rs.getString(3));
			progetto.setDataInizio(new java.util.Date(rs.getDate(4).getTime()));
			progetto.setDataFine(new java.util.Date(rs.getDate(5).getTime()));
			progetto.setBudget(rs.getDouble(6));
			progetto.setStato(StatoProgetto.valueOf(rs.getString(7)));
			progetto.setIdCliente(rs.getLong(8));
			progetto.setIdResponsabile(rs.getLong(9));
			progetto.setPercentualeCompletamento(rs.getInt(10));
			progetto.setCostoProgetto(rs.getDouble(11));

			progetti.add(progetto);
		}

		return progetti;
	}

}
