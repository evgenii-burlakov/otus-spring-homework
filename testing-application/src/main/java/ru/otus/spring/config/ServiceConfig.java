package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.service.clientData.ClientDataService;
import ru.otus.spring.service.clientData.ConsoleServiceImpl;

@Configuration
public class ServiceConfig {

    @Bean
    public ClientDataService consoleServiceImpl() {
        return new ConsoleServiceImpl(System.out, System.in);
    }
}