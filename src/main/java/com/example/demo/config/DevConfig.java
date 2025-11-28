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

    public DevConfig() {
        System.out.println("======================================================");
        System.out.println(">>> PERFIL DEV: CLASSE DevConfig FOI CARREGADA <<<");
        System.out.println("======================================================");
    }

    @Bean
    public boolean instantiateDatabase() {
        System.out.println(">>> PERFIL DEV: TENTANDO EXECUTAR instantiateDatabase() <<<"); // Mensagem atualizada
        
        // MUDANÇA AQUI: Agora verifica se é "create" ou "create-drop"
        if (ddl.equals("create") || ddl.equals("create-drop")) {
            System.out.println(">>> PERFIL DEV: CONDIÇÃO 'create' ou 'create-drop' ATENDIDA (ddl=" + ddl + "). CHAMANDO DBService... <<<"); // Mensagem atualizada
            this.dbService.instanciaDB();
            System.out.println(">>> PERFIL DEV: DBService.instanciaDB() EXECUTADO. <<<"); // Nova mensagem
            return true;
        }
        
        System.out.println(">>> PERFIL DEV: CONDIÇÃO 'create' ou 'create-drop' NÃO ATENDIDA (ddl=" + ddl + "). CARGA DE DADOS CANCELADA. <<<"); // Mensagem atualizada
        return false;
    }
}