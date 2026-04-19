package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * Rakenduse kävitamise meetod, jooksuta enne käsureal  käsku: docker-compose up -d db
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}