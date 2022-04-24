package com.hvs.api.tracks.utils.components.configurations;


import com.hvs.api.tracks.utils.components.handlers.ApiSpotifyResponseErrorHandler;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
public class AppConfig {


    private long requestTimeout;

    private String uriTrackBase;
    private String uriTrackSave;
    private String uriSpotifyBase;
    private String uriSearch;



    @Bean
    public RestTemplate restTemplate() {
        var builder = new RestTemplateBuilder();
        return builder.setReadTimeout(Duration.ofMillis(requestTimeout))
                // .errorHandler(new ApiSpotifyResponseErrorHandler())
                .build();
    }

}
