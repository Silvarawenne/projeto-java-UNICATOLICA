package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.example.demo.services.DBService;

@Configuration
@Profile("dev")
public class DevConfig {

    @Autowired
    private DBService dbService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    // ADICIONE ESTE CONSTRUTOR PARA VERIFICAR SE A CLASSE É CARREGADA
    public DevConfig() {
        System.out.println("======================================================");
        System.out.println(">>> PERFIL DEV: CLASSE DevConfig FOI CARREGADA <<<");
        System.out.println("======================================================");
    }

    @Bean
    public boolean instanciaDB() {
        System.out.println(">>> PERFIL DEV: TENTANDO EXECUTAR instanciaDB() <<<");
        
        if (ddl.equals("create")) {
            System.out.println(">>> PERFIL DEV: CONDIÇÃO 'create' ATENDIDA. CHAMANDO DBService... <<<");
            this.dbService.instanciaDB();
            return true;
        }
        
        System.out.println(">>> PERFIL DEV: CONDIÇÃO 'create' NÃO ATENDIDA. CARGA DE DADOS CANCELADA. <<<");
        return false;
    }
}