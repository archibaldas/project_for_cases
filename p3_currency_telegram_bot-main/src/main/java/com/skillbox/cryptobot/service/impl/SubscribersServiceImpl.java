package com.skillbox.cryptobot.service.impl;

import com.skillbox.cryptobot.exception.NoFoundEntityException;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.repository.SubscribersRepository;
import com.skillbox.cryptobot.service.SubscribersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribersServiceImpl implements SubscribersService {

    private final SubscribersRepository repository;


    @Override
    @Transactional
    public void create(Subscriber subscriber) {
        repository.save(subscriber);
    }

    @Override
    public Boolean isExistsByTelegramId(Long telegramId) {
        return repository.existsByTelegramId(telegramId);
    }

    private Subscriber getByTgId(Long tgId){
        return repository
                .findByTelegramId(tgId)
                .orElseThrow(() ->
                        new NoFoundEntityException("Subscriber with tgId: "+ tgId + " no found"));
    }

    @Override
    @Transactional
    public void updatePrice(Long telegramId, Double price) {
        Subscriber updatedSubscriber;
        try {
            updatedSubscriber = getByTgId(telegramId);
            updatedSubscriber.setPrice(price);
        } catch (NoFoundEntityException e){
            log.warn("{}: Create new.", e.getMessage());
            updatedSubscriber = new Subscriber();
            updatedSubscriber.setTelegramId(telegramId);
            updatedSubscriber.setPrice(price);
        }
        repository.save(updatedSubscriber);
    }

    @Override
    public Double getSubscribePrice(Long telegramId) throws NoFoundEntityException{
        Subscriber subscriber = getByTgId(telegramId);
        return subscriber.getPrice();
    }

    @Override
    public List<Subscriber> findAll() {
        return repository.findAll();
    }

    @Override
    public void updateNotificationTime(Long telegramId) {
        Subscriber subscriber = getByTgId(telegramId);
        subscriber.setLastNotification(LocalDateTime.now());
        repository.save(subscriber);
    }
}
