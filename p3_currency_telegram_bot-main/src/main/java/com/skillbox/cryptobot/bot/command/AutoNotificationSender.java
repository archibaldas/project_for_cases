package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.service.SubscribersService;
import com.skillbox.cryptobot.utils.TextUtil;
import com.skillbox.cryptobot.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class AutoNotificationSender {

    private final AbsSender absSender;
    private final CryptoCurrencyService service;
    private final SubscribersService subscribersService;

    @Scheduled(cron = "${telegram.bot.notify.update-cron}")
    public void autoSend(){
        SendMessage notification = new SendMessage();
        List<Subscriber> subscribers = subscribersService.findAll();
        try {
            double bitcoinPrice = service.getBitcoinPrice();
            if (!subscribers.isEmpty()) {
                List<Subscriber> actual = subscribers.stream()
                        .filter((ss -> ss.getPrice() != null &&
                                bitcoinPrice <= ss.getPrice()))
                        .filter(ss -> {
                            LocalDateTime lastNotification = ss.getLastNotification();
                            LocalDateTime limitTime = TimeUtils.getLimitTime();
                            return lastNotification == null || limitTime.isAfter(lastNotification);
                        })
                        .toList();
                actual.forEach(ss -> {
                    subscribersService.updateNotificationTime(ss.getTelegramId());
                    notification.setChatId(ss.getChatId());
                    notification.setText("Пора покупать, стоимость биткоина " + TextUtil.toString(bitcoinPrice) + " USD");
                    try {
                        absSender.execute(notification);
                        log.info("Subscriber: {} send message with course: {}. Last message: {}", ss.getTelegramId(), bitcoinPrice, ss.getLastNotification());
                    } catch (TelegramApiException e) {
                        log.error("Ошибка возникла /get_price методе", e);
                    }
                });
            }
            log.info(Double.toString(bitcoinPrice));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}
