package com.example.demo.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService;

    // Construtor que recebe AuthenticationManager, JWTUtil e UserDetailsService
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

    // Este método é executado para cada requisição
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String header = request.getHeader("Authorization"); // Pega o cabeçalho Authorization

		// Verifica se o cabeçalho existe e começa com "Bearer "
		if (header != null && header.startsWith("Bearer ")) {
			// Tenta obter o token de autenticação a partir do token JWT
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7)); // Remove "Bearer "
			// Se a autenticação foi bem-sucedida, define-a no contexto de segurança
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		chain.doFilter(request, response); // Continua o filtro
	}

    // Método auxiliar para obter o token de autenticação a partir do token JWT
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) { // Valida o token JWT
			String username = jwtUtil.getUsername(token); // Pega o username (email) do token
			// Carrega os detalhes do usuário usando o UserDetailsService
			UserDetails user = userDetailsService.loadUserByUsername(username);
			// Retorna um token de autenticação do Spring Security com os detalhes do usuário e suas autoridades
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}