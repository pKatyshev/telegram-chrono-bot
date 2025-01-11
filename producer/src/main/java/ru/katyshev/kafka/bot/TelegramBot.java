package ru.katyshev.kafka.bot;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.katyshev.kafka.dto.CommandDTO;
import ru.katyshev.kafka.dto.EventDTO;
import ru.katyshev.kafka.sender.KafkaSender;

import java.util.ArrayList;
import java.util.List;

@Log
@Component
@EnableKafka
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;
    private final KafkaSender kafkaSender;

    private static final String START = "/start";
    private static final String REPORT = "Отчет";
    private static final String DELETE = "Удалить мои данные";
    private static final String INTRODUCTION = "Это бот для подсчета личного времени";
    private static final String INSTRUCTION = "Начиная какое либо действие выберете его на клавиатуре - начнётся отсчет времение для него. Выбрав другое действиее начинается отсчет для него и заканчивается отсчет для первого, и тд. Если занятия нет на экране можно записать с обычной клавиатуры.\nЕсли в ответ бот присылает \"echo: {ваше сообщение}\" значит для действия {ваше сообщение} пошел отсчет времени. Если бот не отвечает - технические шоколадки :(\n\nНажмите конпку \"Отчет\" чтобы получить отчет о затраченном времени.\n\nНажмите конпку \"Пауза\" чтобы поставить хронометраж на паузу.\n\nНажмите конпку \"Удалить мои данные\" чтобы удалить все данные и сбросить хронометр";

    @Autowired
    public TelegramBot(@Value("${bot.token}") String botToken, KafkaSender kafkaSender) {
        super(botToken);
        this.kafkaSender = kafkaSender;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("new message");
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case START -> startCommand(chatId);
                case REPORT -> reportCommand(chatId);
                case DELETE -> deleteCommand(chatId);
                default -> eventCommand(chatId, messageText);
            }
        }

    }

    private void eventCommand(long chatId, String messageText) {
        EventDTO eventDTO = EventDTO.builder()
                .chatId(chatId)
                .event(messageText)
                .build();

        kafkaSender.sendEvent(eventDTO);

        sendMessage(chatId, "echo: " + messageText);
    }

    private void startCommand(long chatId) {
        sendReplyKeyboardMarkUp(chatId);
        sendMessage(chatId,INTRODUCTION);
        sendInstruction(chatId);
    }

    private void reportCommand(long chatId) {
        CommandDTO command = CommandDTO.builder()
                .command("report")
                .chatId(chatId)
                .build();
        kafkaSender.sendCommand(command);
    }

    private void deleteCommand(long chatId) {
        CommandDTO command = CommandDTO.builder()
                .command("delete")
                .chatId(chatId)
                .build();
        kafkaSender.sendCommand(command);
    }

    private void sendInstruction(long chatId) {
        sendMessage(chatId,INSTRUCTION);
    }

    private void sendReplyKeyboardMarkUp(Long chatId) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Сон");
        row1.add("Пробуждение");
        row1.add("Еда");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Соцсети");
        row2.add("Работа");
        row2.add("Интернет");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Отдых");
        row3.add("Что-то");
        row3.add("Что-то");

        KeyboardRow row4 = new KeyboardRow();
        row4.add("Пауза");
        row4.add("Удалить мои данные");
        row4.add("Отчет");

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        SendMessage message = new SendMessage();

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
        message.setText("Привет!");
        message.setChatId(chatId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Long chatId, String textToSend){
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
