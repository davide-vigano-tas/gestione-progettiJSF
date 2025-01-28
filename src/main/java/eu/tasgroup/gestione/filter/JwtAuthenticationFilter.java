package eu.tasgroup.gestione.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.businesscomponent.security.JwtUtil;

public class JwtAuthenticationFilter implements Filter {
    private JwtUtil jwtUtil;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        jwtUtil = new JwtUtil();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Recupera l'header "Authorization"
        String authHeader = httpRequest.getHeader("Authorization");

        // Verifica se l'header è presente e inizia con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Token mancante o non valido\"}");
            return;
        }

        // Estrai il token dall'header
        String token = authHeader.substring(7); // Rimuove "Bearer "

        try {
            // Verifica la validità del token
            if (!jwtUtil.isTokenValid(token)) {
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\": \"Token non valido o scaduto\"}");
                return;
            }

            // Token valido, aggiungi informazioni sull'utente alla richiesta (se necessario)
            String username = jwtUtil.extractUsername(token);
            httpRequest.setAttribute("username", username);

            // Continua con la richiesta
            chain.doFilter(request, response);

        } catch (Exception e) {
            // Gestione errori durante la validazione del token
            httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\": \"Errore nella validazione del token\"}");
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        // Nessuna risorsa da liberare
    }
}
