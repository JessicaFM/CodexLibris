package com.codexlibris;



import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CodexlibrisApiApplication {

	public static void main(String[] args) {
            Logger logger = LoggerFactory.getLogger("com.example.wtfcheck");
            logger.info("🔍 ¿Se cargó application-docker.yml?");

		SpringApplication.run(CodexlibrisApiApplication.class, args);
	}
        @PostConstruct
        public void logStartupInfo() {
            System.out.println("🔍 Redis host configurado: " + System.getProperty("spring.redis.host"));
        }
        

}
