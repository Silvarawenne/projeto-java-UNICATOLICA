package com.example.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Import para addFilterBefore

import org.springframework.security.web.authentication.www.BasicAuthenticationFilter; // Import para addFilterBefore
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// IMPORTANTE: Ajuste esses imports para os seus pacotes JWT e UserDetailsService
import com.example.demo.security.jwt.JWTAuthenticationFilter;
import com.example.demo.security.jwt.JWTAuthorizationFilter;   
import com.example.demo.security.jwt.JWTUtil;                  
import com.example.demo.services.security.UserDetailsServiceImpl; 


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	// URLs que não precisam de autenticação (H2 e POSTs públicos)
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};
	
    // URLs POST que devem ser abertas (e que não usam @PreAuthorize)
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/login", 
			"/clientes"
			// REMOVIDO /tecnicos daqui para ser protegido pelo filtro JWT!
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		// Configuração para perfil 'dev' (acesso ao H2 Console)
		if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
			http.headers().frameOptions().disable();
		}
		
		// Desabilita CSRF (necessário para APIs stateless)
		http.cors().and().csrf().disable();
		
		// Define as URLs abertas e o padrão de autenticação
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest().authenticated(); 
		
        // === CORREÇÃO FINAL: INJEÇÃO ORDENADA DOS FILTROS JWT ===
		
        // 1. Adiciona o filtro de autenticação (Login) - Não requer ordem específica, mas adicionamos primeiro
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		// 2. Adiciona o filtro de autorização (Leitura do Token) ANTES do filtro padrão do Spring
		// Isso garante que o SecurityContextHolder seja populado ANTES da checagem de @PreAuthorize.
		http.addFilterBefore(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService), BasicAuthenticationFilter.class);
		
		// Configura a política de sessão como STATELESS (Essencial para JWT)
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}