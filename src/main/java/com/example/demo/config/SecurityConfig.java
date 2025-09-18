package com.example.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // NOVO IMPORT
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity; // NOVO IMPORT (para @PreAuthorize)
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter; // Já existe
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService; // NOVO IMPORT
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// IMPORTANTE: Ajuste esses imports para os seus pacotes JWT e UserDetailsService
import com.example.demo.security.jwt.JWTAuthenticationFilter; // AJUSTE O PACOTE SE NECESSÁRIO
import com.example.demo.security.jwt.JWTAuthorizationFilter;   // AJUSTE O PACOTE SE NECESSÁRIO
import com.example.demo.security.jwt.JWTUtil;                  // AJUSTE O PACOTE SE NECESSÁRIO
// Import do seu UserDetailsServiceImpl
import com.example.demo.services.security.UserDetailsServiceImpl; // AJUSTE O PACOTE SE NECESSÁRIO


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Habilita anotações como @PreAuthorize
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService; // Injete seu UserDetailsService
	
	@Autowired
	private JWTUtil jwtUtil; // Injete seu JWTUtil
	
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};
	
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/login", // O filtro de autenticação JWT vai interceptar este
			"/clientes",
			"/tecnicos"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		// Permite acesso ao H2 Console em perfil dev
		if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable(); // Configura CORS e desabilita CSRF
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() // Permite POSTs específicos
			.antMatchers(PUBLIC_MATCHERS).permitAll() // Permite outros públicos
			.anyRequest().authenticated(); // Qualquer outra requisição exige autenticação
		
		// Adiciona o filtro de autenticação JWT
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		// Adiciona o filtro de autorização JWT
		// Este deve vir antes do filtro de autenticação padrão para interceptar tokens em requisições subsequentes
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Sessão stateless para JWT
	}
	
	// Configura o AuthenticationManager para usar seu UserDetailsService e BCryptPasswordEncoder
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    // Configuração do CORS (se você precisar de uma configuração mais detalhada para o front-end)
    // Se você já tem uma configuração de CORS global em outro lugar, pode remover este @Bean.
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS")); // Permite todos os métodos
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration); // Aplica a configuração a todos os caminhos
		return source;
	}
}