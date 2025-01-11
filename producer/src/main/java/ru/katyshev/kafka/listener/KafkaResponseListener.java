package ru.katyshev.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.katyshev.kafka.bot.TelegramBot;
import ru.katyshev.kafka.dto.ServerResponseDTO;

@Component
@EnableKafka
public class KafkaResponseListener {
    private final TelegramBot telegramBot;

    private static final String RESPONSE_TOPIC = "response";

    @Autowired
    public KafkaResponseListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @KafkaListener(topics = RESPONSE_TOPIC)
    public void eventListener(@Payload ServerResponseDTO serverResponseDTO) {
        telegramBot.sendMessage(serverResponseDTO.getChatId(), serverResponseDTO.getResponse());
    }

}
