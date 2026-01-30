package com.skillbox.cryptobot.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static java.time.LocalDateTime.now;

@Component
@ConfigurationProperties("telegram.bot.notify.delay")
@Getter
@Setter
@NoArgsConstructor
public class TimeUtils {

    private static int value = 10;
    private static ChronoUnit unit = ChronoUnit.MINUTES;

    public static LocalDateTime getLimitTime(){
        return now().minus(value, unit);
    }

}
