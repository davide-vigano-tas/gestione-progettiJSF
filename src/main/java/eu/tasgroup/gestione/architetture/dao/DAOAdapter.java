package eu.tasgroup.gestione.architetture.dao;

import java.sql.Connection;

public abstract class DAOAdapter<T> implements GenericDAO<T> {

	@Override
	public void create(Connection conn, T entity) throws DAOException {
	}

	@Override
	public void update(Connection conn, T entity) throws DAOException {
	}

	@Override
	public void delete(Connection conn, long id) throws DAOException {
	}

	@Override
	public T getById(Connection conn, long id) throws DAOException {
		return null;
	}

	@Override
	public T[] getAll(Connection conn) throws DAOException {
		return null;
	}

}
