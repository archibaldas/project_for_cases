package ru.skillbox.currency.exchange.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.currency")
@Getter
@Setter
public class CurrencyConfig {
    private String cbrUrl;
}
