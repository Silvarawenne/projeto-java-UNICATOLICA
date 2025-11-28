package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import com.example.demo.services.DBService;

@Configuration
@Profile("prod") // <--- Importante: Só roda quando estiver no Render (Produção)
public class ProdConfig {

    @Autowired
    private DBService dbService;

    // Verifica a configuração do application-prod.properties
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Bean
    public boolean instantiateDatabase() {
        // Se estiver como "create" OU "update", nós tentamos criar os dados
        if ("create".equals(ddl) || "update".equals(ddl)) {
            try {
                dbService.instanciaDB();
                System.out.println(">>> BANCO DE DADOS POPULADO COM SUCESSO NO RENDER! <<<");
            } catch (Exception e) {
                System.out.println(">>> DADOS JÁ EXISTEM OU ERRO AO POPULAR (Isso é normal se já rodou antes) <<<");
            }
            return true;
        }
        return false;
    }
}