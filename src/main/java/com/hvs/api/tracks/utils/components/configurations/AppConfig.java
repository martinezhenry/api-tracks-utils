package com.hvs.api.tracks.utils.components.configurations;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "config")
@Data
public class AppConfig {


    private long requestTimeout;
    private String uriTrackBase;
    private String uriTrackSave;
    private String uriSpotifyBase;
    private String uriSpotifyBaseToken;
    private String uriSpotifySearch;
    private String uriSpotifyToken;
    private String encodedCredentials;

}
