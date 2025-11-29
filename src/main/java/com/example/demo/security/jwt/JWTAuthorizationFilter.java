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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter; // Herança que se encaixa no addFilterBefore

import io.jsonwebtoken.Claims; 

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService; 

	// Construtor que recebe as dependências
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
			// Remove "Bearer "
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7)); 
			if (auth != null) {
				// ESSENCIAL: Injeta a autenticação no contexto
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response);
	}

    // MÉTODO getAuthentication (LÓGICA STATELESS)
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
            
			// REQUER QUE getClaims() em JWTUtil seja PUBLIC!
			Claims claims = jwtUtil.getClaims(token);
            
            if (claims == null) {
                return null;
            }

			String username = claims.getSubject();
            
            // 1. Extrai a lista de roles (Lendo o Claim 'roles')
            // Assumimos que rolesRaw contém Strings como "ROLE_ADMIN"
            List<?> rolesRaw = claims.get("roles", List.class);
            
            // 2. Converte as roles (Strings) para GrantedAuthority
         // JWTAuthorizationFilter.java (Método getAuthentication)

         // ...

         // 2. Converte as roles (Strings) para GrantedAuthority
         @SuppressWarnings("unchecked")
         Collection<GrantedAuthority> authorities = rolesRaw.stream()
             // Mapeia cada role (String) para um objeto SimpleGrantedAuthority
             .map(role -> new SimpleGrantedAuthority((String) role)) 
             // FINALIZA o stream e coleta o resultado em uma lista
             .collect(Collectors.toList()); 

         // Retorna o token de autenticação (STATELSS)
         return new UsernamePasswordAuthenticationToken(username, null, authorities);

         // ...
		}
		return null;
	}
}