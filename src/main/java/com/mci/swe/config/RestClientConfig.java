package com.mci.swe.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 1) HttpClient bauen
        CloseableHttpClient httpClient = HttpClients.custom()
            // hier kannst Du Timeouts, Connection-Pool, usw. konfigurieren
            .build();

        // 2) Factory mit Deinem HttpClient
        HttpComponentsClientHttpRequestFactory rf =
            new HttpComponentsClientHttpRequestFactory(httpClient);

        // 3) RestTemplate bauen
        RestTemplate restTemplate = new RestTemplate(rf);

        // 4) Jackson-Converter so anpassen, dass er auch text/plain verarbeiten kann
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jacksonConv) {
                // aktuelle Mediatypes kopieren und text/plain hinzufügen
                List<MediaType> supported = new ArrayList<>(jacksonConv.getSupportedMediaTypes());
                supported.add(MediaType.TEXT_PLAIN);
                jacksonConv.setSupportedMediaTypes(supported);
            }
        }

        return restTemplate;
    }
}
