package eu.tasgroup.gestione.businesscomponent.utility;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.facade.ClienteFacade;
import eu.tasgroup.gestione.businesscomponent.model.Payment;
import eu.tasgroup.gestione.businesscomponent.model.Project;
import eu.tasgroup.gestione.businesscomponent.model.ProjectTask;
import eu.tasgroup.gestione.businesscomponent.model.User;

public class InvoicePDFGenerator {

	public static void generatePDF(OutputStream out, Payment payment)
			throws IOException, DAOException, NamingException {
		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage();
			document.addPage(page);

			// Recupero il progetto e i dettagli del cliente
			Project project = ClienteFacade.getInstance().getProjectById(payment.getIdProgetto());
			User cliente = ClienteFacade.getInstance().getById(project.getIdCliente());

			// Costanti per il calcolo IVA
			final double IVA_PERCENT = 22.0; // 22%
			double importoBase = payment.getCifra() / (1 + (IVA_PERCENT / 100));
			double importoIVA = payment.getCifra() - importoBase;

			try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
				// Aggiunge le varie sezioni del PDF
				addHeader(contentStream, payment);
				addClientDetails(contentStream, cliente);
				addPaymentDetails(contentStream, payment, importoBase, importoIVA, IVA_PERCENT);
				addFooter(contentStream);
			}

			// Scrivi il documento nel flusso di output
			document.save(out);
		}
	}

	private static void addHeader(PDPageContentStream contentStream, Payment payment) throws IOException {
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
		contentStream.showText("Fattura n. " + payment.getId());
		contentStream.newLine();
		contentStream.showText("Data: " + LocalDateTime.now());
		contentStream.endText();
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
		contentStream.showText("Nome: " + project.getNomeProgetto() + ". Budget: " + project.getBudget() + ". Costo: "
				+ project.getCostoProgetto());
		contentStream.newLine();
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream.showText("Descrizione: " + project.getDescrizione() + ". Completamento: "
				+ project.getPercentualeCompletamento());
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

	private static void addPaymentDetails(PDPageContentStream contentStream, Payment payment, double importoBase,
			double importoIVA, double IVA_PERCENT) throws IOException {
		float startX = 50;
		float startY = 550;
		float rowHeight = 20;

		// Intestazioni tabella
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
		contentStream.beginText();
		contentStream.newLineAtOffset(startX, startY);
		contentStream.showText("Dettagli Pagamento:");
		contentStream.endText();

		float currentY = startY - rowHeight;
		String[][] rows = { { "Importo Base (escluso IVA)", String.format("%.2f", importoBase) },
				{ "IVA (" + IVA_PERCENT + "%)", String.format("%.2f", importoIVA) },
				{ "Totale", String.format("%.2f", payment.getCifra()) } };

		// Disegna righe tabella
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		for (String[] row : rows) {
			contentStream.beginText();
			contentStream.newLineAtOffset(startX, currentY);
			contentStream.showText(row[0]);
			contentStream.newLineAtOffset(300, 0);
			contentStream.showText(row[1]);
			contentStream.endText();
			currentY -= rowHeight;
		}
	}

	private static void addFooter(PDPageContentStream contentStream) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(50, 370);
		contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
		contentStream.showText("Note:");
		contentStream.newLine();
		contentStream.setFont(PDType1Font.HELVETICA, 12);
		contentStream
				.showText("Grazie per aver scelto la nostra azienda. Rimaniamo a disposizione per eventuali domande.");
		contentStream.endText();
	}

	public static void generatePDF(OutputStream out, Project project) throws ServletException {
		try (PDDocument document = new PDDocument()) {
			PDPage page = new PDPage();

			document.addPage(page);

			// Recupero responsabile e cliente
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
				throw new ServletException(e.getMessage());
			}

			// Scrivi il documento nel flusso di output
			document.save(out);
		} catch (DAOException | NamingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ServletException(e.getMessage());
		}
	}

	private static void addTasksDetails(PDPageContentStream contentStream, List<ProjectTask> tasks, float startY)
			throws IOException {
		String[] headers = { "ID", "Nome", "Dipendente", "Stato", "Scadenza" };
		String[][] rows = new String[tasks.size()][headers.length];
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		float[] columnWidths = new float[] { 30, 100, 100, 100, 100 };
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
			rows[i] = new String[] { String.valueOf(task.getId()), task.getNomeTask(),
					String.valueOf(task.getIdDipendente()), task.getStato().name(),
					formato.format(task.getScadenza()) };
		}

		drawTable(contentStream, rows, startY, new float[] { 30, 100, 100, 100, 100 });
	}

	private static void drawTable(PDPageContentStream contentStream, String[][] rows, float startY,
			float[] columnWidths) throws IOException {
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
