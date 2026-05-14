package academy.devdojo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource")
public record ConnectionConfigurationProperties(String url, String username, String password) {
}
