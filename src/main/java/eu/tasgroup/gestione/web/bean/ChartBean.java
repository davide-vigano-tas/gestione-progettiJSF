package eu.tasgroup.gestione.web.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.NamingException;

import eu.tasgroup.gestione.architetture.dao.DAOException;
import eu.tasgroup.gestione.businesscomponent.facade.AdminFacade;
import eu.tasgroup.gestione.businesscomponent.model.Project;

@Named
@ViewScoped
public class ChartBean implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6896633183644789723L;
	private Map<String, List<String>> dataMap;
	
	

	
    public Map<String, List<String>> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, List<String>> dataMap) {
		this.dataMap = dataMap;
	}

	public ChartBean() throws DAOException, NamingException {
        // Sample data: Projects and their completion percentages
        dataMap = new HashMap<>();
        Project[] projects = AdminFacade.getInstance().getAllProjects();
        String[] names = new String[projects.length];
        String[] values = new String[projects.length];
        for(int i = 0; i<projects.length; i++) {
        	names[i] = projects[i].getNomeProgetto();
        	values[i] = String.valueOf(projects[i].getPercentualeCompletamento());
        	
        }
        dataMap.put("labels", Arrays.asList(names));
        dataMap.put("values", Arrays.asList(values)); // Completion percentages
    }

    public String getChartJson() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("{\n");
    	sb.append("\"type\": \"bar\",\n");
    	sb.append("\"data\": {\n");
    	sb.append("\"labels\": [");
    	for (String label : dataMap.get("labels")) {
    	    sb.append("\"").append(label).append("\",");
    	}
    	if (!dataMap.get("labels").isEmpty()) sb.setLength(sb.length() - 1); // Remove last comma
    	sb.append("],\n");

    	sb.append("\"datasets\": [\n");
    	sb.append("{\n");
    	sb.append("\"label\": \"Percentuale completamento\",\n"); // Corrected label key
    	sb.append("\"data\": [");
    	for (String val : dataMap.get("values")) {
    	    sb.append(val).append(",");
    	}
    	if (!dataMap.get("values").isEmpty()) sb.setLength(sb.length() - 1); // Remove last comma
    	sb.append("],\n");
    	sb.append("\"borderWidth\": 1,\n");
    	sb.append("\"backgroundColor\": [\"CornflowerBlue\"]\n");
    	sb.append("}\n");
    	sb.append("]\n");
    	sb.append("},\n");

    	sb.append("\"options\": {\n");
    	sb.append("\"responsive\": true,\n");
    	sb.append("\"scales\": {\n");
    	sb.append("\"y\": {\n");
    	sb.append("\"max\": 100,\n");
    	sb.append("\"beginAtZero\": true\n");
    	sb.append("}\n");
    	sb.append("}\n");
    	sb.append("}\n");
    	sb.append("}");
    	System.err.println("Error: "+sb.toString());
    	return sb.toString();

    }
}
