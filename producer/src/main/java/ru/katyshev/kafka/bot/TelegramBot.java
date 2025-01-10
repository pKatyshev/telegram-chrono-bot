package ru.katyshev.kafka.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.katyshev.kafka.dto.EventDTO;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken, KafkaTemplate<String, Object> kafkaTemplate) {
        super(botToken);
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("message");
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            System.out.println(messageText);

            EventDTO eventDTO = new EventDTO();
            eventDTO.setEvent(messageText);
            eventDTO.setChatId(chatId);
            kafkaTemplate.send("evnt", "0", eventDTO);

            sendMessage(chatId, "echo: " + messageText);
        }

    }

    private void sendMessage(Long chatId, String textToSend){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }
}
