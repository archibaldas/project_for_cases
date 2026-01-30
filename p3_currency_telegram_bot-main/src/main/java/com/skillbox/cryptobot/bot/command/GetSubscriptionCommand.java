package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.exception.NoFoundEntityException;
import com.skillbox.cryptobot.service.SubscribersService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
@AllArgsConstructor
public class GetSubscriptionCommand implements IBotCommand {

    private final SubscribersService subscribersService;

    @Override
    public String getCommandIdentifier() {
        return "get_subscription";
    }

    @Override
    public String getDescription() {
        return "Возвращает текущую подписку";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        SendMessage answer = new SendMessage();
        answer.setChatId(message.getChatId());
        Long userId = message.getFrom().getId();

        try {
            Double price = subscribersService.getSubscribePrice(userId);
            if (price != null) {
                answer.setText("Вы подписаны на стоимость биткойна " + TextUtil
                        .toString(price) + " USD");
            } else {
                answer.setText("Активные подписки отсутствуют");
            }
        } catch (NoFoundEntityException e){
            log.error(e.getMessage());
        }

        try {
            absSender.execute(answer);
        } catch (TelegramApiException e) {
            log.error("Error occurred in /start command", e);
        }
    }
}