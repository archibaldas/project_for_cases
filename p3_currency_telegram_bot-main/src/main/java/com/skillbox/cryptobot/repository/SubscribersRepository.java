package com.skillbox.cryptobot.repository;

import com.skillbox.cryptobot.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscriber, UUID> {
    Boolean existsByTelegramId(Long telegramId);
    Optional<Subscriber> findByTelegramId(Long telegramId);
}
