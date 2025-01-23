package eu.tasgroup.gestione.businesscomponent.security;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Algoritmo {

	private static final SecureRandom random = new SecureRandom();
	private static final int SALT_LENGTH = 16;
	
	public static String converti(String password) {
		try {
			// Genera sale
			byte[] salt = new byte[SALT_LENGTH];
			random.nextBytes(salt);
			String saltString = Base64.getEncoder().encodeToString(salt);
			
			//Mischiare il sale e la password
			
			byte[] saltPasswordBytes = mixSaltAndPassword(salt, password);
			
			// Esegui hashing della combinazione sale pass. Attenti a non rendere vulnerabile l'hashing da a ttacchi
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = md.digest(saltPasswordBytes);
			
			//Conversione i byte hash in string esadecimale
			StringBuilder hexString = new StringBuilder();
			for(byte b : hashedBytes) {
				hexString.append(String.format("%02x", b));
			}
			
			// restituisco il sale e l'hash come una singola stringa
			
			return saltString + ":" +hexString.toString();
			
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 non trovato", e);
		}
	}
	
	public static boolean verificaPassword(String password, String storedHash) {
		try {
			//Estrarre sale da stringa memorizzata
			String[] parts = storedHash.split(":");
			String saltString = parts[0];
			String hashString = parts[1];
			
			// decodifica sale
			byte[] salt = Base64.getDecoder().decode(saltString);
			
			// Mischiare il sale e la password
			byte[] saltPasswordBytes = mixSaltAndPassword(salt, password);
			// Eseguo l'hashing della combinazioe sale-password
			
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedBytes = md.digest(saltPasswordBytes);
			
			//Converti i byte hash in stringa esadecimale
			
			StringBuilder hexString = new StringBuilder();
			for(byte b : hashedBytes) {
				hexString.append(String.format("%02x", b));
			}
			
			//Confronta l'hash calcolato con quello memorizzato
			
			return hashString.equals(hexString.toString());
			
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("SHA-256 non trovato", e);
		}
	}

	private static byte[] mixSaltAndPassword(byte[] salt, String password) {
		byte[] passwordBytes = password.getBytes(Charset.forName("UTF-8"));
		byte[] saltPasswordBytes = new byte[salt.length+passwordBytes.length];
		
		int i = 0, j = 0;
		
		while(i< passwordBytes.length && i < salt.length) {
			saltPasswordBytes[j++] = passwordBytes[i];
			saltPasswordBytes[j++] = salt[i];
			i++;
		}
		
		
		while(i<passwordBytes.length) {
			
			saltPasswordBytes[j++] = passwordBytes[i++];
		}
		
		while(i<salt.length) {
			
			saltPasswordBytes[j++] = salt[i++];
		}
		
		return saltPasswordBytes;
		
	}
	
}
