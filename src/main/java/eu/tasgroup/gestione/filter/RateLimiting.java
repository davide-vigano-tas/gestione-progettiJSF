package eu.tasgroup.gestione.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.tasgroup.gestione.businesscomponent.security.RateLimiter;

@WebFilter("/*")
public class RateLimiting extends HttpFilter implements Filter {

	private static final long serialVersionUID = -1416076908701794559L;
	private RateLimiter rateLimiter;
	
	public void init(FilterConfig fConfig) throws ServletException {
		//Il client se supera richeste al secondo viene bloccato per 5 secondi
		//Cambiamo per provare
		rateLimiter = new RateLimiter(15, 1, 5);
	}



	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpResp = (HttpServletResponse) response;
		String clientIP = httpReq.getRemoteAddr();
		
		if(rateLimiter.isRateLimited(clientIP)) {
			//Too many request
			httpResp.setStatus(429);
			httpReq.getRequestDispatcher("/error429.xhtml").forward(httpReq, httpResp);
		} else {
			chain.doFilter(request, response);
		}

	}




}
