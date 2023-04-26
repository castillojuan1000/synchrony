package com.castillojuan.synchrony.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.castillojuan.synchrony.exception.InvalidJwtException;
import com.castillojuan.synchrony.service.JwtService;

import ch.qos.logback.core.subst.Token;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtService jwtService; 
	@Autowired
	private final UserDetailsService userDetailsService;
	

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain)throws ServletException, IOException {
		
		boolean isLoginRequest = request.getRequestURI().contains("/login") && request.getRequestURI().endsWith("/login");
		boolean isRegisterUser = request.getRequestURI().contains("/register") && request.getRequestURI().endsWith("/register");
		final String authHeader = request.getHeader("Authorization");
		final String jwt; 
		String userName;
		
		boolean invalidAuthHeader = false;
		boolean invalidJwt = false;
		
		if(isLoginRequest || isRegisterUser) {
			filterChain.doFilter(request, response);
	        return;
	        
		}else if(authHeader == null || !authHeader.startsWith("Bearer ")) {	
			
			invalidAuthHeader = true;
			
		}else {
			
			jwt = authHeader.substring(7);
			
			try {
		        userName = jwtService.extractUsername(jwt);
		    } catch (InvalidJwtException e) {
		        invalidJwt = true;
		        userName = null;
		    }
			
			if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);
				
				if(jwtService.isTokenValid(jwt, userDetails)) {
					
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
				
			}
		}
		
		
		if(invalidAuthHeader) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"message\": \"Invalid or missing Authorization header\", \"status\": 400}");
	       
		}else if(invalidJwt){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    response.setContentType("application/json");
		    response.getWriter().write("{\"message\": \"Invalid JWT token\", \"status\": 401}");
		}else {
			filterChain.doFilter(request, response);
		}
		
	}

}
