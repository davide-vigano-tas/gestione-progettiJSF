package eu.tasgroup.gestione.conf;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;

//Attvio pagine jsf nel progetto
@FacesConfig(version= Version.JSF_2_3)
//Devo dire che ha visbilità applicazione
@ApplicationScoped
public class JSFActivator {

}
