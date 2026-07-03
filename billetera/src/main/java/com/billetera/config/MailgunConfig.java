package com.billetera.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailgunConfig {

    @Value("${mailgun.api-key}")
    private String apiKey;

    /**
     * Crea un interceptor que agrega autenticación Basic Auth
     * con las credenciales de Mailgun codificadas en Base64.
     *
     * @return interceptor de peticiones Feign
     */
    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return requestTemplate -> {
            String auth = "api:" + apiKey;
            String encoded = java.util.Base64.getEncoder().encodeToString(auth.getBytes());
            requestTemplate.header("Authorization", "Basic " + encoded);
        };
    }
}
