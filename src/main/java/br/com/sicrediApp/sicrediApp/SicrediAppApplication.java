package br.com.sicrediApp.sicrediApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SicrediAppApplication implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(SicrediAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar se a tabela "usuario" existe no banco de dados
        List<Map<String, Object>> usuarios = jdbcTemplate.queryForList("SELECT * FROM usuario");

        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário encontrado no banco de dados.");
        } else {
            System.out.println("Usuários encontrados no banco de dados:");
            for (Map<String, Object> usuario : usuarios) {
                System.out.println("ID: " + usuario.get("id") + ", Nome: " + usuario.get("nome" )+ ", CPF: " + usuario.get("cpf"));
            }
        }
    }
}
