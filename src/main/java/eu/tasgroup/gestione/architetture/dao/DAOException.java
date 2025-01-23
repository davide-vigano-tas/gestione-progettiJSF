package eu.tasgroup.gestione.architetture.dao;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DAOException extends SQLException {
	private static final long serialVersionUID = -3150338052504493383L;

	private static final Logger logger = Logger.getLogger(DAOException.class.getName());

	static {
		try {
			String logDirPath = "c:\\log";
			File logDir = new File(logDirPath);
			if (!logDir.exists()) {
				logDir.mkdir();
			}

			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(Level.SEVERE);
			consoleHandler.setFormatter(new SimpleFormatter());

			FileHandler fileHandler = new FileHandler(logDirPath + "\\dao-exception-log", true);
			fileHandler.setLevel(Level.SEVERE);
			fileHandler.setFormatter(new SimpleFormatter());

			logger.addHandler(consoleHandler);
			logger.addHandler(fileHandler);

			logger.setLevel(Level.SEVERE);

			logger.setUseParentHandlers(false);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Impossibile inizializzare gli handler del logger", e);
		}

	}

	public DAOException(SQLException sql) {
		super(sql);
		log(sql);
	}

	@Override
	public String getMessage() {
		String sqlMessage = super.getMessage();
		String sqlState = getSQLState();

		if (sqlMessage == null)
			return "Eccezione SQL non prevista. Codice errore: " + getErrorCode() + ", Stato SQL: " + sqlState;

		Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("23000", "Violazione di vincolo della tabella");
		errorMessages.put("08001", "Impossibile stabilire una connessione con il database");
		errorMessages.put("42000", "Errore di sintassi SQL o violazione delle restrizioni di accesso");
		errorMessages.put("HY000", "Errore generale del server MySQL");

		for (Map.Entry<String, String> entry : errorMessages.entrySet()) {
			if (sqlMessage.contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		return "Eccezione SQL non prevista. Messaggio: " + sqlMessage;

	}

	private void log(SQLException sql) {
		logger.log(Level.SEVERE, "Codice Errore SQL: {0}, Stato SQL: {1}, Messaggio: {2}, Causa: {3}",
				new Object[] { sql.getErrorCode(), sql.getSQLState(), sql.getMessage(), sql.getCause() });
	}
}
