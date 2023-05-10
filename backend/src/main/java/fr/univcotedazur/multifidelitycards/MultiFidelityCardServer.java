package fr.univcotedazur.multifidelitycards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MultiFidelityCardServer {
    public static void main(String[] args) {
        SpringApplication.run(MultiFidelityCardServer.class, args);
    }
}
