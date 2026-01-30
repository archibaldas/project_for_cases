package com.skillbox.cryptobot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "subscribers")
public class Subscriber {

    @Id
    @UuidGenerator
    private UUID uuid;

    @Column(name = "telegram_id", nullable = false)
    private Long telegramId;

    @Column(name = "price")
    private Double price;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "last_notification")
    private LocalDateTime lastNotification;

}
