package com.finops.alert.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "alert.threshold")
public class AlertConfig {
    private BigDecimal dailyIncreasePercent = BigDecimal.valueOf(20);
    private BigDecimal monthlyBudget = BigDecimal.valueOf(10000);
}
