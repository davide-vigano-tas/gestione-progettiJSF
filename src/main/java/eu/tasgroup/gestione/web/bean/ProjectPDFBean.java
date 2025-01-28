package eu.tasgroup.gestione.web.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.NamingException;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.User;

@Named
@RequestScoped
public class ProjectPDFBean implements Serializable {
	


	  /**
	 * 
	 */
	private static final long serialVersionUID = 4192638727307194328L;

	public void downloadPdfProject(long id) throws IOException, DAOException, NamingException {
		  	System.err.println("Called");
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        facesContext.responseComplete();

	        // Setup the response
	        OutputStream responseOutput = facesContext.getExternalContext().getResponseOutputStream();
	        facesContext.getExternalContext().setResponseContentType("application/pdf");
	        facesContext.getExternalContext().setResponseHeader("Content-Disposition", "attachment; filename=\"example.pdf\"");

	        // Generate the PDF using PDFBox
	        try (PDDocument document = new PDDocument();
	             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
	        	 PDPage page = new PDPage();
	             
	             document.addPage(page);

	             // Recupero responsabile e cliente
	             Project project = AdminFacade.getInstance().getProjectById(id);
	             User resp = AdminFacade.getInstance().getUserById(project.getIdResponsabile());
	             User cliente = AdminFacade.getInstance().getUserById(project.getIdCliente());

	             // recupero task
	             List<ProjectTask> tasks = AdminFacade.getInstance().getTasksByProject(project);
	    

	             try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
	                 // Aggiunge le varie sezioni del PDF
	             	 addHeaderProject(contentStream, project);
	                 addManagerDetails(contentStream, resp);
	                 addClientDetails(contentStream, cliente);
	                 addTasksDetails(contentStream, tasks, 500);
	                 contentStream.beginText();
	                 contentStream.newLineAtOffset(50, 370);
	                 contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	                 contentStream.showText("Note:");
	                 contentStream.newLine();
	                 contentStream.setFont(PDType1Font.HELVETICA, 12);
	                 contentStream.showText("Report admin");
	                 contentStream.endText();
	             } catch (IOException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 				throw new DAOException(new SQLException(e.getMessage()));
	 			}

	             // Scrivi il documento nel flusso di output
	             document.save(byteArrayOutputStream);

	            // Write PDF to response output
	            responseOutput.write(byteArrayOutputStream.toByteArray());
	        } finally {
	            responseOutput.flush();
	            responseOutput.close();
	            facesContext.responseComplete();
	            facesContext.renderResponse();
	        }
	    }
	  
	    private static void addHeaderProject(PDPageContentStream contentStream, Project project) throws IOException {
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
	        contentStream.beginText();
	        contentStream.setLeading(14f);
	        contentStream.newLineAtOffset(50, 750);
	        contentStream.showText("Azienda Gestione Progetti S.r.l.");
	        contentStream.newLine();
	        contentStream.showText("Via Esempio, 123 - 20100 Milano (MI)");
	        contentStream.newLine();
	        contentStream.showText("P.IVA: 12345678901");
	        contentStream.newLine();
	        contentStream.showText("Email: info@azienda.com | Telefono: +39 012 345 678");
	        contentStream.newLine();
	        contentStream.newLine();
	        contentStream.setFont(PDType1Font.HELVETICA, 12);
	        contentStream.showText("Progetto n. " + project.getId());
	        contentStream.newLine();
	        contentStream.setFont(PDType1Font.HELVETICA, 12);
	        contentStream.showText("Nome: " + project.getNomeProgetto() + ". Budget: "+project.getBudget()+ 
	        		". Costo: "+project.getCostoProgetto());
	        contentStream.newLine();
	        contentStream.setFont(PDType1Font.HELVETICA, 12);
	        contentStream.showText("Descrizione: " + project.getDescrizione() + ". Completamento: "+project.getPercentualeCompletamento());
	        contentStream.newLine();
	        contentStream.endText();
	    }

	    private static void addClientDetails(PDPageContentStream contentStream, User cliente) throws IOException {
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	        contentStream.beginText();
	        contentStream.newLineAtOffset(50, 640);
	        contentStream.showText("Dettagli Cliente:");
	        contentStream.newLine();
	        contentStream.setFont(PDType1Font.HELVETICA, 12);
	        contentStream.showText("Nome: " + cliente.getNome() + " " + cliente.getCognome());
	        contentStream.newLine();
	        contentStream.showText("Email: " + cliente.getEmail());
	        contentStream.newLine();
	        contentStream.endText();
	    }
	    
	    private static void addManagerDetails(PDPageContentStream contentStream, User cliente) throws IOException {
	        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	        contentStream.beginText();
	        contentStream.newLineAtOffset(50, 550);
	        contentStream.showText("Dettagli Manager:");
	        contentStream.newLine();
	        contentStream.setFont(PDType1Font.HELVETICA, 12);
	        contentStream.showText("Nome: " + cliente.getNome() + " " + cliente.getCognome());
	        contentStream.newLine();
	        contentStream.showText("Email: " + cliente.getEmail());
	        contentStream.newLine();
	        contentStream.endText();
	    }
	    
	    private static void addTasksDetails(PDPageContentStream contentStream, List<ProjectTask> tasks, float startY) throws IOException {
	        String[] headers = {"ID", "Nome", "Dipendente", "Stato", "Scadenza"};
	        String[][] rows = new String[tasks.size()][headers.length];
	        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
	        float[] columnWidths = new float[]{30, 100, 100, 100, 100};
	        float xPosition = 50;
	        
	        for (int i = 0; i < headers.length; i++) {
	        	
	            contentStream.beginText();
	            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	            contentStream.newLineAtOffset(xPosition, startY);
	            contentStream.showText(headers[i] != null ? headers[i] : "");
	            contentStream.endText();
	            xPosition += columnWidths[i];
	        }
	        startY -= 20;
	        for (int i = 0; i < tasks.size(); i++) {
	            ProjectTask task = tasks.get(i);
	            rows[i] = new String[]{
	                String.valueOf(task.getId()),
	                task.getNomeTask(),
	                String.valueOf(task.getIdDipendente()),
	                task.getStato().name(),
	                formato.format(task.getScadenza())
	            };
	        }

	        drawTable(contentStream, rows, startY, new float[]{30, 100, 100, 100, 100});
	    }

	    private static void drawTable(PDPageContentStream contentStream, String[][] rows, float startY, float[] columnWidths) throws IOException {
	        float yPosition = startY;
	        for (String[] row : rows) {
	            float xPosition = 50;
	            for (int i = 0; i < row.length; i++) {
	                contentStream.beginText();
	                contentStream.setFont(PDType1Font.HELVETICA, 8);
	                contentStream.newLineAtOffset(xPosition, yPosition);
	                contentStream.showText(row[i] != null ? row[i] : "");
	                contentStream.endText();
	                xPosition += columnWidths[i];
	            }
	            yPosition -= 20;
	        }
	    }
}
