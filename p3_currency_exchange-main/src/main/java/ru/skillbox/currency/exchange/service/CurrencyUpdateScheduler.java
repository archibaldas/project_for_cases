package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyUpdateScheduler {
    private final CurrencyService currencyService;

    @Scheduled(cron = "${app.currency.update-crone: 0 0 * * * *}")
    public void updateCurrencies(){
        log.info("Запуск запланированного обновления курсов валют");
        currencyService.updateCurrencies();
    }
}
