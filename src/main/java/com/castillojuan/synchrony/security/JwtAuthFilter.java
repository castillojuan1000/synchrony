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
		
		final String authHeader = request.getHeader("Authorization");
		final String jwt; 
		final String userName;
		boolean invalidAuthHeader = false;
		
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {	
			invalidAuthHeader = true;
		}else {
			jwt = authHeader.substring(7);
			
			userName = jwtService.extractUsername(jwt);
			
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
	        response.getWriter().write("{\"message\": \"Invalid or missing Authorization header\", \"status\": \"400\"}");
	       
		}else {
			filterChain.doFilter(request, response);
		}
		
	}

}
