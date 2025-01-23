package eu.tasgroup.gestione.businesscomponent.security;

public class EscapeHTML {

	
	public static String escapeHtml(String input) {
		if(input == null) return null;
		StringBuilder escape = new StringBuilder();
		for(char c : input.toCharArray()) {
			switch(c) {
			case '<':
				escape.append("&lt;");
			break;
			case '>':
				escape.append("&gt;");
			break;
			case '&':
				escape.append("&amp;");
			break;
			case '"':
				escape.append("&quote;");
			break;
			case '\'':
				escape.append("&#x27;");
			break;
			case '/':
				escape.append("&#x2f;");
			break;
			default:
				escape.append(c);
			}
		}
		return escape.toString();
	}
}
