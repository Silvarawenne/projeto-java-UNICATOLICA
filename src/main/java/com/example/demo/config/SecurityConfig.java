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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter; // Import para addFilterBefore
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// IMPORTS: Ajuste os imports abaixo se o pacote for diferente
import com.example.demo.security.jwt.JWTAuthenticationFilter;
import com.example.demo.security.jwt.JWTAuthorizationFilter;   
import com.example.demo.security.jwt.JWTUtil;                  
import com.example.demo.services.security.UserDetailsServiceImpl; 


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
			"/login", 
			"/clientes"
			// O POST /tecnicos DEVE SER REMOVIDO DAQUI
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
			.anyRequest().authenticated(); 
		
        // === SOLUÇÃO FINAL: GARANTIR A ORDEM DO FILTRO DE AUTORIZAÇÃO ===
		
		// 1. Adiciona o filtro de autenticação (Login)
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		// 2. Adiciona o filtro de autorização ANTES do filtro padrão de autenticação básica.
		// Isso garante que o token seja lido e o ROLE_ADMIN injetado no SecurityContext a tempo.
		http.addFilterBefore(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService), BasicAuthenticationFilter.class);
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	// O restante da classe (configure(AuthenticationManagerBuilder) e Beans) está correto.
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// O CorsConfigurationSource também está OK, mas adicionamos applyPermitDefaultValues
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}