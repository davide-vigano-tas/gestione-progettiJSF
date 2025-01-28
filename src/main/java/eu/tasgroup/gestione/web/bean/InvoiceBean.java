package eu.tasgroup.gestione.web.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.PaymentBC;
import eu.tasgroup.gestione.businesscomponent.utility.InvoicePDFGenerator;

@Named
@ViewScoped
public class InvoiceBean implements Serializable {

	private static final long serialVersionUID = 5299362190526227279L;

	public String downloadInvoicePDF(long id) {

		System.out.println("Sono dentro con ID: " + id);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		externalContext.setResponseContentType("application/pdf");
		externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"fattura_" + id + ".pdf\"");

		try (OutputStream out = externalContext.getResponseOutputStream()) {
			// Genera il PDF
			PaymentBC paymentBC = new PaymentBC();
			
			System.out.println("Recupero il pagamento " + paymentBC.getById(id));
			System.out.println("Genero PDF");
			InvoicePDFGenerator.generatePDF(out, paymentBC.getById(id));
			System.out.println("PDF generato \n" + out );
			
			// Forza lo svuotamento del buffer di output
		    out.flush();
			
			System.out.println("Segnalo risposta al client");
			// Segnala la risposta al client
			facesContext.responseComplete();
			return null;
		} catch (IOException | DAOException | NamingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
