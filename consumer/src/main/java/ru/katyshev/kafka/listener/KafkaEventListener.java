package ru.katyshev.kafka.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.katyshev.kafka.dto.CommandDTO;
import ru.katyshev.kafka.dto.EventDTO;
import ru.katyshev.kafka.dto.ServerResponseDTO;
import ru.katyshev.kafka.model.Event;
import ru.katyshev.kafka.service.EventService;

@Component
@EnableKafka
public class KafkaEventListener {
    private final EventService eventService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String KEY = "0";
    private static final String EVENT_TOPIC = "evnt";
    private static final String COMMAND_TOPIC = "command";
    private static final String RESPONSE_TOPIC = "response";

    @Autowired
    public KafkaEventListener(EventService eventService,
                              KafkaTemplate<String, Object> kafkaTemplate) {
        this.eventService = eventService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(id = "app.1", topics = EVENT_TOPIC)
    public void eventListener(@Payload EventDTO dto) {
        Event e = new Event();
        e.setChatId(dto.getChatId());
        e.setEvent(dto.getEvent());

        eventService.save(e);
    }

    @KafkaListener(topics = COMMAND_TOPIC)
    public void eventListener(@Payload CommandDTO commandDTO) {
        System.out.println("TOPIC COMMAND");
        ServerResponseDTO responseDTO;
        String command = commandDTO.getCommand();

        switch (command) {
            case "report" -> {
                System.out.println("REPORT");
                responseDTO = eventService.getReport(commandDTO.getChatId());
                kafkaTemplate.send(RESPONSE_TOPIC, KEY, responseDTO);
            }
            case "delete" -> {
                System.out.println("DELETE");
                responseDTO = eventService.deleteUserData(commandDTO.getChatId());
                kafkaTemplate.send(RESPONSE_TOPIC, KEY, responseDTO);
            }
        }
    }
}
