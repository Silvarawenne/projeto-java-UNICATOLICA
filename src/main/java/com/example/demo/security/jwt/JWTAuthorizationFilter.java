package com.example.demo.security.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims; 


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService; 

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader("Authorization"); 

		if (header != null && header.startsWith("Bearer ")) {
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7)); 
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response);
	}

    // MÉTODO FINALIZADO: LÊ AS PERMISSÕES DIRETAMENTE DO TOKEN
	// JWTAuthorizationFilter.java (Método getAuthentication)

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
	    if (jwtUtil.tokenValido(token)) {
	        
	        // REQUER QUE getClaims() em JWTUtil seja PUBLIC!
	        Claims claims = jwtUtil.getClaims(token);
	        
	        if (claims == null) {
	            return null;
	        }

	        String username = claims.getSubject();
	        
	        // Extrai a lista de roles (A lista de Objetos vem do Claims)
	        @SuppressWarnings("unchecked")
	        List<String> rolesRaw = claims.get("roles", List.class);
	        
	        // Converte as roles (Strings como "ROLE_ADMIN") para SimpleGrantedAuthority
	        // Usamos map com cast explícito, que é o que o Spring Boot costuma esperar
	        Collection<GrantedAuthority> authorities = rolesRaw.stream()
	                .map(role -> new SimpleGrantedAuthority(role)) 
	                .collect(Collectors.toList());
	        
	        // Retorna o token de autenticação sem bater no banco (STATELESS)
	        return new UsernamePasswordAuthenticationToken(username, null, authorities);
	    }
	    return null;
	}
}