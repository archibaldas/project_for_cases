package ru.skillbox.currency.exchange.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.skillbox.currency.exchange.service.CurrencyService;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyUpdateScheduler {
    private final CurrencyService currencyService;


    @Scheduled(cron = "${app.currency.update-cron}")
    public void updateCurrencies(){
        log.info("Запуск запланированного обновления курсов валют");
        currencyService.updateCurrencies();
    }
}
