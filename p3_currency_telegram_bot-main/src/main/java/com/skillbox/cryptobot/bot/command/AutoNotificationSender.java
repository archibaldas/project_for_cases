package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.client.BinanceClient;
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
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class AutoNotificationSender {

    private final AbsSender absSender;
    private final SubscribersService subscribersService;
    private final BinanceClient binanceClient;
    private final TimeUtils timeUtils;

    @Scheduled(cron = "${telegram.bot.notify.update-cron}")
    public void autoSend(){
        SendMessage notification = new SendMessage();
        List<Subscriber> subscribers = subscribersService.findAll();
        try {
            double bitcoinPrice = binanceClient.getBitcoinPrice();
            if (!subscribers.isEmpty()) {
                subscribers.stream()
                        .filter((ss -> ss.getPrice() != null &&
                                bitcoinPrice <= ss.getPrice()))
                        .filter(ss -> ss.getLastNotification() == null ||
                                timeUtils.getLimitTime().isAfter(ss.getLastNotification()))
                        .forEach(ss -> {
                            subscribersService.updateNotificationTime(ss.getTelegramId());
                            notification.setChatId(ss.getChatId());
                            notification.setText("Пора покупать, стоимость биткоина " + TextUtil.toString(bitcoinPrice) + " USD");
                            try {
                                absSender.execute(notification);
                                log.info("Subscriber: {} send message with course: {}. Last message: {}", ss.getTelegramId(), bitcoinPrice, ss.getLastNotification());
                            } catch (TelegramApiException e) {
                                log.error("Ошибка: {} --- в методе (autoSend())", e.getMessage());
                            }
                        });
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }
}
