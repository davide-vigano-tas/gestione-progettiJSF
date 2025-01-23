package eu.tasgroup.gestione.architetture.dao;

public interface DAOConstants {
	/*---------------------- USER - USER ROLE*/

	// SELECT

	String SELECT_USERS = "SELECT * FROM users";
	String SELECT_USER_ID = "SELECT * FROM users WHERE id = ?";
	String SELECT_USER_USERNAME = "SELECT * FROM users WHERE username = ?";
	String SELECT_USER_EMAIL = "SELECT * FROM users WHERE email = ?";
	String SELECT_DIPENDENTI_NON_ASSEGNATI = "select d.id, d.nome, d.cognome, d.username, d.password"
			+ ", d.email, d.tentativi_falliti, d.locked, d.data_creazione from users d join users_roles r"
			+ " on d.id = r.id_users "
			+ "where r.role = 'DIPENDENTE' and not exists(select * from project_tasks t where t.id_dipendente = d.id)";

	String SELECT_USER_ROLES_BY_USERNAME = "SELECT r.role FROM users u "
	        + "JOIN users_roles r ON u.id = r.id_users "
	        + "WHERE u.username = ?";
	
	String SELECT_USER_ROLES_BY_USER_ID = "SELECT r.role FROM users u "
	        + "JOIN users_roles r ON u.id = r.id_users "
	        + "WHERE u.id = ?";
	String SELECT_USERS_BY_SKILL = "select u.id, u.nome, u.cognome, u.username, u.password, u.email, u.tentativi_falliti, "
			+ "u.locked, u.data_creazione from user_skills uk"
			+ "   join users u on u.id = uk.id_utente join skills sk on sk.id = uk.id_competenze where sk.tipo = ?";
	
	String SELECT_USERS_BY_ROLES = "select u.id, u.nome, u.cognome, u.username, u.password, u.email, u.tentativi_falliti, "
			+ "u.locked, u.data_creazione "
			+ "FROM users u JOIN users_roles r ON u.id = r.id_users WHERE r.role = ?";

	// INSERT
	String INSERT_USERS = "INSERT INTO users (nome, cognome, username, password, email) VALUES (?, ?, ?, ?, ?)";
	String INSERT_USERS_ROLES = "INSERT INTO users_roles (id_users, role) VALUES (?, ?)"; 

	// UPDATE
	String UPDATE_USER = "UPDATE users SET nome = ?, cognome = ?, username = ?, "
	        + "email = ?, tentativi_falliti = ?, locked = ? WHERE id = ?";
	String UPDATE_USER_ROLES = "UPDATE users_roles SET role = ? WHERE id_users = ? AND role = ?";
	

	//DELETE
	String DELETE_USER = "delete from users where id = ?";
	String DELETE_USER_ROLE = "delete from users_roles where role = ? and id_users = ?";

	/*---------------------- PROJECT*/

	// SELECT
	String SELECT_PROJECTS = "SELECT * FROM projects";
	String SELECT_PROJECT_ID = "SELECT * FROM projects WHERE id = ?";
	String SELECT_PROJECT_STATUS = "SELECT * FROM projects WHERE stato = ?";
	String SELECT_PROJECTS_CLIENTE = "SELECT * FROM projects WHERE id_cliente = ?";
	String SELECT_PROJECTS_RESPONSABILE = "SELECT * FROM projects WHERE id_responsabile = ?";
	String SELECT_PROJECT_COMPLETAMENTO = "SELECT percentuale_completamento FROM projects WHERE id = ?";

	
	// INSERT
	String INSERT_PROJECT = "INSERT INTO projects (nome_progetto, descrizione, data_inizio, data_fine, budget, id_cliente, "
	        + "id_responsabile, costo_progetto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	// UPDATE
	String UPDATE_PROJECT = "UPDATE projects SET nome_progetto = ?, descrizione = ?, "
	        + "data_fine = ?, budget = ?, stato = ?, id_responsabile = ?, percentuale_completamento = ? WHERE id = ?";
	
	// DELETE
	String DELETE_PROJECT = "DELETE FROM projects WHERE id = ?";
	
	/*------------------- PAYMENT*/
	
	// SELECT
	String SELECT_PAYMENTS = "select * from payments";
	String SELECT_PAYMENT_BY_ID = "select * from payments where id = ?";
	String DELETE_PAYMENT = "delete from payments where id = ?";
	String SELECT_PAYMENTS_BY_PROJECT = "select * from payments where id_progetto = ?";
	String SELECT_PAYMENTS_BY_USER = "select p.id, p.id_progetto, p.cifra from "
			+ "payments p join projects pr on pr.id = p.id_progetto where pr.id_cliente = ?";
	
	// INSERT
	String INSERT_PAYMENT = "insert into payments(id_progetto, cifra) values(?, ?)";

	
	/*------------------- SKILL*/
	
	
	// SELECT
	String SELECT_SKILLS = "select * from skills";
	String SELECT_SKILL = "select * from skills where id = ?";
	String SELECT_SKILLS_BY_TIPO = "select * from skills where tipo = ?";
	String SELECT_SKILLS_BY_USER = "select s.id, s.tipo from user_skills uk join skills s on"
			+ " uk.id_competenze = s.id where uk.id_utente = ?"; 
	// INSERT
	String INSERT_SKILL = "insert into skills(tipo) value(?)";
	// DELETE
	String DELETE_SKILL = "delete from skills where id = ?";
	String DELETE_SKILLS_BY_TIPO ="delete from skills where tipo = ?";
	
	/*------------------- USER_SKILL*/
	
	
	// SELECT
	String SELECT_USERS_SKILLS = "select * from user_skills";
	String SELECT_USER_SKILL = "select * from user_skills where id = ?";
	
	// INSERT
	String INSERT_USER_SKILL = "insert into user_skills(id_competenze, id_utente) values(?, ?)";
	
	// DELETE
	String DELETE_USER_SKILL ="delete from user_skills where id = ?"; 
	
	
	
	//   --project tasks

	String INSERT_PROJECT_TASK = "insert into project_tasks (id_progetto, nome_task, descrizione, id_dipendente, scadenza, fase) values (?, ?, ?, ?, ?, ?);";

	String SELECT_PROJECT_TASKS = "select * from project_tasks;";
	String SELECT_PROJECT_TASK = "select * from project_tasks where id = ?;";
	String SELECT_PROJECT_TASKS_BY_PROJECT = "select * from project_tasks where id_progetto = ?;";
	String SELECT_PROJECT_TASKS_BY_DIPENDENTE = "select * from project_tasks where id_dipendente = ?;";
	String SELECT_PROJECT_TASKS_BY_PROJECT_MANAGER = "SELECT "
            + "    pt.id AS task_id, "
			+ "    pt.id_progetto,"
            + "    pt.nome_task, "
            + "    pt.descrizione AS task_descrizione, "
            + "    pt.id_dipendente, "
            + "    pt.stato AS task_stato, "
            + "    pt.scadenza AS task_scadenza, "
            + "    pt.fase AS task_fase "
            + "FROM "
            + "    project_tasks pt "
            + "JOIN "
            + "    projects p ON pt.id_progetto = p.id "
            + "JOIN "
            + "    users pm ON p.id_responsabile = pm.id "
            + "WHERE "
            + "    pm.id = ?;";  


	String UPDATE_PROJECT_TASK = "update project_tasks set nome_task = ?, descrizione = ?, id_dipendente = ?, stato = ?, scadenza = ?, fase = ? where id = ?;";
	
	String UPDATE_PROJECT_TASK_DIPENDENTE = "update project_tasks set id_dipendente = ? where id = ?;";
	String UPDATE_PROJECT_TASK_FASE = "update project_tasks set fase = ? where id = ?;";
	String UPDATE_PROJECT_TASK_STATO = "update project_tasks set stato = ? where id = ?;";

	String DELETE_PROJECT_TASK = "delete from project_tasks where id = ?;";



	//   -- timesheets

	String INSERT_TIMESHEET = "insert into timesheets (id_dipendente, id_progetto, id_task, ore_lavorate, data) values (?, ?, ?, ?, ?);";

	String SELECT_TIMESHEETS = "select * from timesheets;";
	String SELECT_TIMESHEET = "select * from timesheets where id = ?;";
	String SELECT_TIMESHEET_BY_DIPENDENTE = "select * from timesheets where id_dipendente =?;";
	String SELECT_TIMESHEET_BY_PROJECT = "select * from timesheets where id_progetto = ?;";
	String SELECT_TIMESHEETS_BY_PROJECT_MANAGER ="SELECT t.* FROM timesheets t "
			+ "JOIN project_tasks pt ON t.id_task = pt.id JOIN projects p ON t.id_progetto = p.id WHERE p.id_responsabile = ?";

	String UPDATE_TIMESHEET = "update timesheets set id_dipendente = ?, id_progetto = ?, id_task = ?, ore_lavorate = ?, data = ? where id = ?;";
	String APPROVA_TIMESHEET = "update timesheets set approvato = ? where id = ?;";

	String DELETE_TIMESHEET = "delete from timesheets where id = ?;";



	//    --audit logs

	String INSERT_AUDIT_LOG = "insert into audit_logs (utente, operazione, data) values (?, ?, ?);";

	String SELECT_AUDIT_LOGS = "select * from audit_logs";
	
	String SELECT_AUDIT_LOG = "select * from audit_logs where id = ?";

	String UPDATE_AUDT_LOG = "update audit_logs set utente = ?, operazione = ?, data = ? where id = ?;";

	String DELETE_AUDIT_LOG = "delete from audit_logs where id = ?;";


	
	//--------------Ticket
	
	String INSERT_TICKET = "insert into tickets(opener, title, description, created_at) values(?, ?, ?, ?)";
	String UPDATE_TICKET = "update tickets set title = ?, description = ? where id = ?";
	String SELECT_TICKETS = "select * from tickets";
	String SELECT_TICKETS_BY_DIP = "select * from tickets where opener = ?";
	String SELECT_TICKET = "select * from tickets where id = ?";
	String CLOSE_TICKET = "update tickets set closed_at = ? where id = ?";
	String DELETE_TICKET = "delete from tickets where id = ?";
	String SELECT_OPEN = "select * from tickets where closed_at is null";
	String SELECT_CLOSED = "select * from tickets where closed_at is not null";



	
	
}