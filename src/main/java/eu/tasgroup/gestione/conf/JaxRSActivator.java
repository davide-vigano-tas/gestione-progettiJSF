package eu.tasgroup.gestione.conf;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
//Per servizi rest
//Acceso ai servizi da questo url
//Non riconosce in automatico il nome del progetto, va messo nel pom in warName
@ApplicationPath("/api")
public class JaxRSActivator extends Application{
	
}
