package eu.tasgroup.gestione.businesscomponent.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {

	// Chiave segreta codificata in Base64 (dovrebbe essere configurata
	// dinamicamente in produzione)
	private String secretKey = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

	// Durata del token in millisecondi (3600_000 = 1 ora)
	private long jwtExpiration = 3600_000;

	/**
	 * Estrae l'username (subject) dal token JWT.
	 *
	 * @param token il token JWT
	 * @return l'username contenuto nel token
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Estrae un claim specifico dal token JWT.
	 *
	 * @param <T>            il tipo del claim da restituire
	 * @param token          il token JWT
	 * @param claimsResolver la funzione per risolvere il claim
	 * @return il valore del claim
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Genera un token JWT con l'username come subject.
	 *
	 * @param username l'username dell'utente
	 * @return il token JWT generato
	 */
	public String generateToken(String username) {
		return generateToken(new HashMap<>(), username);
	}

	/**
	 * Genera un token JWT con claim aggiuntivi.
	 *
	 * @param extraClaims claim extra da includere nel token
	 * @param username    l'username dell'utente
	 * @return il token JWT generato
	 */
	public String generateToken(Map<String, Object> extraClaims, String username) {
		return buildToken(extraClaims, username, jwtExpiration);
	}

	/**
	 * Costruisce il token JWT con i dettagli specificati.
	 *
	 * @param extraClaims claim extra da includere
	 * @param username    l'username dell'utente
	 * @param expiration  tempo di scadenza del token
	 * @return il token JWT
	 */
	private String buildToken(Map<String, Object> extraClaims, String username, long expiration) {
		return Jwts.builder().setClaims(extraClaims).setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Verifica se il token JWT è valido.
	 *
	 * @param token il token JWT
	 * @return true se il token è valido, false altrimenti
	 */
	public boolean isTokenValid(String token) {
		return !isTokenExpired(token);
	}

	/**
	 * Verifica se il token JWT è scaduto.
	 *
	 * @param token il token JWT
	 * @return true se il token è scaduto, false altrimenti
	 */
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Estrae la data di scadenza dal token JWT.
	 *
	 * @param token il token JWT
	 * @return la data di scadenza
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Estrae tutti i claim dal token JWT.
	 *
	 * @param token il token JWT
	 * @return i claim contenuti nel token
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	/**
	 * Restituisce la chiave per firmare i token JWT.
	 *
	 * @return la chiave HMAC
	 */
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	// Getter e setter per configurare dinamicamente secretKey e jwtExpiration

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public long getJwtExpiration() {
		return jwtExpiration;
	}

	public void setJwtExpiration(long jwtExpiration) {
		this.jwtExpiration = jwtExpiration;
	}
}
