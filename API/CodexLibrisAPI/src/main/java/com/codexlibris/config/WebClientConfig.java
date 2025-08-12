/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codexlibris.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author jessica
 */
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(ObjectMapper objectMapper) {
        ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper)))
            .build();

        return WebClient.builder()
            .exchangeStrategies(strategies)
            .build();
    }
}