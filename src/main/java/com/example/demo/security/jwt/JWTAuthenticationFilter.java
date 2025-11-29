package com.example.demo.security.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.domain.dtos.CredenciaisDTO; // Sua classe DTO para credenciais de login

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

    // Método que tenta autenticar o usuário
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			// Tenta converter o JSON do corpo da requisição em um objeto CredenciaisDTO
			CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);

            // Cria um token de autenticação com o email e senha
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getSenha(), new ArrayList<>());

            // Delega a autenticação para o AuthenticationManager do Spring Security
			Authentication authenticate = authenticationManager.authenticate(authenticationToken);
			return authenticate;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    // Método chamado em caso de autenticação bem-sucedida
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		// Pega o usuário logado (UserSS)
		UserSS user = (UserSS) authResult.getPrincipal();
		String username = user.getUsername();
		
		// NOVO: Extrai as roles (perfis) do usuário e transforma em Lista de Strings
		// Necessário importar: java.util.stream.Collectors e org.springframework.security.core.GrantedAuthority
		List<String> roles = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());

		// Gera o token passando o username E as roles
		String token = jwtUtil.generateToken(username, roles);
		
		// Adiciona o token no cabeçalho da resposta
		response.setHeader("access-control-expose-headers", "Authorization");
		response.setHeader("Authorization", "Bearer " + token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {

		response.setStatus(401); // Define o status HTTP como 401 (Unauthorized)
		response.setContentType("application/json"); // Define o tipo de conteúdo como JSON
		response.getWriter().write(json().toString()); // Converte para String; // Escreve o JSON de erro na resposta
	}

    // Método auxiliar para gerar um JSON de erro
	private CharSequence json() {
		long date = new Date().getTime();
		return "{" 
				+ "\"timestamp\": " + date + ", " 
				+ "\"status\": 401, "
				+ "\"error\": \"Não autorizado\", " 
				+ "\"message\": \"Email ou senha inválidos\", "
				+ "\"path\": \"/login\"}";
	}
}