package com.finops.alert.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "slack")
public class SlackConfig {
    private String webhookUrl;
    private String channel = "#finops-alerts";
    private String username = "FinOps Bot";
    private String iconEmoji = ":moneybag:";
}
