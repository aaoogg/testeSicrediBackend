package br.com.sicrediApp.sicrediApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SicrediAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SicrediAppApplication.class, args);
        System.out.println("Servidor Online");
    }
}
