package eu.tasgroup.gestione.conf;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

public class CustomErrorHandler extends ExceptionHandlerWrapper {
	
	private ExceptionHandler exceptionHandler;
	
	
	

	public CustomErrorHandler(ExceptionHandler wrapper) {
		super(wrapper);
		this.exceptionHandler=wrapper;
	}

	

	//Richiamato in jsf 

	@Override
	public ExceptionHandler getWrapped() {
		// TODO Auto-generated method stub
		return exceptionHandler;
	}




	@Override
	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> coda = getUnhandledExceptionQueuedEvents().iterator();
		while(coda.hasNext()) {
			ExceptionQueuedEvent elemento = coda.next();
			//Elemento del contesto sul quale viene lanciata l'eccezione
			ExceptionQueuedEventContext exceptionQueueEventContext = (ExceptionQueuedEventContext) elemento.getSource();
			try {
				Throwable eccezione = exceptionQueueEventContext.getException();
				System.out.println("Exception"+eccezione.getMessage());
				//Contesto della pagina jsf
				FacesContext contesto = FacesContext.getCurrentInstance();
				
				Map<String, Object> mappa = contesto.getExternalContext().getRequestMap();
				
				//Per redirect jsf, navigare sulle pagine
				NavigationHandler  nav = contesto.getApplication().getNavigationHandler();
				
				mappa.put("error-message",  eccezione.getMessage());
				mappa.put("error-stack",  eccezione.getStackTrace());
				
				nav.handleNavigation(contesto, null,  "/error");
				contesto.renderResponse();
			}finally {
				coda.remove();
			}
		}

	}

}
