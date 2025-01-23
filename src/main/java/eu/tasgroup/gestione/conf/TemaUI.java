package eu.tasgroup.gestione.conf;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named
public class TemaUI {
	public String getTema() {
		return "saga";
	}
}

