package eu.tasgroup.gestione.architecture.dbaccess;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import eu.tasgroup.gestione.architetture.dao.DAOException;

public class DBAccess {

    private static DataSource dataSource;
    private static InitialContext testContext; // For injecting custom context during tests

    public static void setTestContext(InitialContext context) {
        testContext = context;
    }

    public static synchronized Connection getConnection() throws NamingException, DAOException {
        try {
            if (dataSource == null) {
                InitialContext contesto = testContext != null ? testContext : new InitialContext();
                dataSource = (DataSource) contesto.lookup("java:/MySQLDS");
            }
            return dataSource.getConnection();
        } catch (SQLException sql) {
            throw new DAOException(sql);
        }
    }

    public static void closeConnection(Connection conn) throws DAOException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException sql) {
            throw new DAOException(sql);
        }
    }
}
