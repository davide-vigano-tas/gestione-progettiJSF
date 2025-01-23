package eu.tasgroup.gestione.web.bean;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.AuditLogBC;
import eu.tasgroup.gestione.businesscomponent.PaymentBC;
import eu.tasgroup.gestione.businesscomponent.ProjectBC;
import eu.tasgroup.gestione.businesscomponent.UserBC;
import eu.tasgroup.gestione.businesscomponent.enumerated.Ruoli;
import eu.tasgroup.gestione.businesscomponent.model.AuditLog;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.Role;
import eu.tasgroup.gestione.businesscomponent.model.User;

@Named
@RequestScoped
public class ClienteFacadeBean {

    @Inject
    private UserBC userBC;
    @Inject
    private ProjectBC projectBC;
    @Inject
    private PaymentBC paymentBC;
    @Inject
    private AuditLogBC auditLogBC;

    public User createOrUpdateCliente(User user) throws DAOException, NamingException {
        User created = userBC.createOrUpdate(user);
        if (user.getId() == 0) {
            Role role = new Role();
            role.setRole(Ruoli.CLIENTE);
            userBC.addRole(created, role);
        }
        return created;
    }

    public User getById(long id) throws DAOException, NamingException {
        return userBC.getById(id);
    }

    public User getByUsername(String username) throws DAOException, NamingException {
        return userBC.getByUsername(username);
    }

    public User getByEmail(String email) throws DAOException, NamingException {
        return userBC.getByEmail(email);
    }

    public int getPercentualeCompletamentoProjectID(long id) throws DAOException, NamingException {
        return projectBC.getPercentualeCompletamento(id);
    }

    public Project getProjectById(long id) throws DAOException, NamingException {
        return projectBC.getById(id);
    }

    public List<Project> getProjectsByCliente(User user) throws DAOException, NamingException {
        return projectBC.getListProjectByCliente(user.getId());
    }

    public void createPayment(Payment payment) throws DAOException, NamingException {
        paymentBC.create(payment);
    }

    public Payment getPaymentById(long id) throws DAOException, NamingException {
        return paymentBC.getById(id);
    }

    public Payment[] getPaymentByProject(Project project) throws DAOException, NamingException {
        return paymentBC.getByProject(project);
    }

    public Payment[] getPaymentByCliente(User cliente) throws DAOException, NamingException {
        return paymentBC.getByUser(cliente);
    }

    public Role[] getRolesById(long id) throws DAOException, NamingException {
        return userBC.getRolesById(id);
    }

    public double getTotalPaymentsByProjectId(long id) throws DAOException, NamingException {
        Project p = projectBC.getById(id);
        Payment[] payments = paymentBC.getByProject(p);
        double totale = 0;
        for (Payment payment : payments) {
            totale += payment.getCifra();
        }
        return totale;
    }

    public void saveLogMessage(String username, String operazione) throws DAOException, NamingException {
        AuditLog log = new AuditLog();
        log.setUtente(username);
        log.setOperazione(operazione);
        log.setData(new Date());
        auditLogBC.createOrUpdate(log);
    }
}
