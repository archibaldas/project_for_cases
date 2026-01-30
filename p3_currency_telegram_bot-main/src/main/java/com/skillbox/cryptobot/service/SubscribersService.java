package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.model.Subscriber;

import java.util.List;

public interface SubscribersService {
    void create(Subscriber subscribers);
    Boolean isExistsByTelegramId(Long telegramId);
    void updatePrice(Long telegramId, Double price);
    Double getSubscribePrice(Long telegramId);
    List<Subscriber> findAll();
    void updateNotificationTime(Long telegramId);
}