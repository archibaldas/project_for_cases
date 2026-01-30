package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.service.SubscribersService;
import com.skillbox.cryptobot.utils.DataExtractor;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Обработка команды подписки на курс валюты
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private final SubscribersService subscribersService;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        Long chatId = message.getChatId();
        answer.setChatId(chatId);
        Long userId = message.getFrom().getId();
        Double price;
        try {
            price = DataExtractor.extractPrice(message.getText());
            subscribersService.updatePrice(userId, price);
            answer.setText("Новая подписка создана на стоимость " + TextUtil.toString(price) + " USD");

        } catch (Exception e) {
            log.error("Subscriber with id: {} das not enter price", message.getFrom().getId());
            answer.setText("""
                    Не введен желаемый курс, пожалуйста введите команду в формате: 
                     /subscribe [число].
                     Пример: /subscribe 340566
                     """);
        }

        try {
            absSender.execute(answer);
            log.info("Subscriber with id: {} subscribed to notification", message.getFrom().getId());
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }

    }
}