package eu.tasgroup.gestione.businesscomponent.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

//Per serializzare un oggetto(javax.json)
//Se io vogio gestire dei cookie, ne devo traformare l'oggetto in testo semplice.
//
public class Serialize {
	public static String toString(Serializable o) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(output);
		
		stream.writeObject(o);
		output.close();
		stream.close();
		
		return Base64.getEncoder().encodeToString(output.toByteArray());
	}
	
	public static Object fromString(String string) throws IOException, ClassNotFoundException {
		byte[] byteObj = Base64.getDecoder().decode(string);
		ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(byteObj));
		
		Object o = stream.readObject();
		stream.close();
		return o;
	}
	
	
}
