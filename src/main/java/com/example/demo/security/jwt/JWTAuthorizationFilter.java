package com.example.demo.security.jwt;

import java.io.IOException;
import java.util.List; // NOVO
import java.util.stream.Collectors; // NOVO
import org.springframework.security.core.authority.SimpleGrantedAuthority; // NOVO
import org.springframework.security.core.GrantedAuthority; // NOVO

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims; // NOVO

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService; // Mantemos para o construtor, mas não usamos mais

    // Construtor
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

    // Este método é executado para cada requisição
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

    // MÉTODO getAuthentication AGORA LÊ AS PERMISSÕES DIRETAMENTE DO TOKEN
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
            
            // 1. Tenta obter os Claims (conteúdo) do token
			Claims claims = jwtUtil.getClaims(token); // <--- ATENÇÃO AQUI!
            
            if (claims == null) {
                return null;
            }

			String username = claims.getSubject();
            
            // 2. Extrai a lista de roles (que gravamos no passo anterior)
            List<?> rolesRaw = claims.get("roles", List.class);
            
            // 3. Converte as roles (String) para GrantedAuthority
            List<GrantedAuthority> authorities = rolesRaw.stream()
                    .map(role -> new SimpleGrantedAuthority((String) role))
                    .collect(Collectors.toList());
            
			// 4. Retorna um novo token de autenticação sem bater no banco (Stateless)
			return new UsernamePasswordAuthenticationToken(username, null, authorities);
		}
		return null;
	}
}