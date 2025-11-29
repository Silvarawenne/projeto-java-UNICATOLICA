package com.example.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.jwt.JWTAuthenticationFilter;
import com.example.demo.security.jwt.JWTAuthorizationFilter;
import com.example.demo.security.jwt.JWTUtil;
import org.springframework.context.annotation.Lazy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Lazy
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // Definição dos endpoints públicos
    private static final String[] PUBLIC_MATCHERS = {
        "/h2-console/**",
        "/clientes/**",
        "/tecnicos/**",
        "/produtos/**",
        "/categorias/**",
        "/pedidos/**"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
        "/clientes",
        "/tecnicos",
        "/clientes/picture" // Se você tiver um endpoint para upload de foto de cliente
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        // Configuração para rodar o H2 Console
        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }

        // Configuração de CORS, desativação de CSRF e modo Stateless
        http.cors().and().csrf().disable();
        
        // Define as regras de autorização
        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .anyRequest().authenticated(); 
        
        // Injeta os filtros de JWT na cadeia de segurança
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
        
        // Injeção AGRESSIVA do filtro de Autorização para evitar 403
        http.addFilterBefore(
            new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService), 
            org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
        );

        // Define a política de sessão como Stateless
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Usamos a instância injetada (bCryptPasswordEncoder)
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================================================================
    // ⚠️ MÉTODO CORRIGIDO PARA LIBERAR O CORS NA URL DO VERSEL
    // =========================================================================
 // SecurityConfig.java

 // ...

 // =========================================================================
 // ⚠️ MÉTODO CORRIGIDO PARA LIBERAR O CORS NA URL DO VERSEL
 // =========================================================================
 @Bean
 CorsConfigurationSource corsConfigurationSource() {
     CorsConfiguration configuration = new CorsConfiguration();
     
     // 1. URLs PERMITIDAS (Manter a sua URL real e localhost)
     configuration.setAllowedOrigins(Arrays.asList(
         "https://helpdeskprojectfrontend.vercel.app", // SUA URL REAL
         "http://localhost:4200",
         "http://localhost:8100" 
     ));

     // 2. MÉTODOS HTTP PERMITIDOS
     configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
     
     // 3. HEADERS PERMITIDOS (CORREÇÃO FINAL: Lista abrangente de Headers Angular)
     configuration.setAllowedHeaders(Arrays.asList(
         "Authorization", 
         "Content-Type",
         "X-Requested-With", // Usado pelo Angular/XMLHttpRequest
         "Accept",           // Padrão de Requisições HTTP
         "Origin"            // Necessário para o pre-flight
     )); 
     
     // 4. PERMITE CREDENCIAIS (Crucial para o Header Authorization ser enviado)
     configuration.setAllowCredentials(true); 

     final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", configuration);
     return source;
 }
}