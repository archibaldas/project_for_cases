package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.exception.NoFoundEntityException;
import com.skillbox.cryptobot.model.Subscriber;
import com.skillbox.cryptobot.service.SubscribersService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды отмены подписки на курс валюты
 */
@Service
@Slf4j
@AllArgsConstructor
public class UnsubscribeCommand implements IBotCommand {

    private final SubscribersService subscribersService;

    @Override
    public String getCommandIdentifier() {
        return "unsubscribe";
    }

    @Override
    public String getDescription() {
        return "Отменяет подписку пользователя";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        try {
            subscribersService.updatePrice(message.getFrom().getId(), null);
            answer.setText("Подписка отменена");
        } catch (NoFoundEntityException e){
            log.error(e.getMessage());
        }
        try {
            absSender.execute(answer);
            log.info("Subscriber with id: {} unsubscribed", message.getFrom().getId());
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }

    }
}