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
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
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
		if (Arrays.asList(env.getActiveProfiles()).contains("dev")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest().authenticated(); // <--- ONDE A ORDEM DOS FILTROS COMEÇA A IMPORTAR MUITO
		
        // === MOVEMOS OS FILTROS JWT PARA DEPOIS DE 'anyRequest().authenticated()' ===
		// Adiciona o filtro de autenticação JWT
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		// Adiciona o filtro de autorização JWT
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		
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