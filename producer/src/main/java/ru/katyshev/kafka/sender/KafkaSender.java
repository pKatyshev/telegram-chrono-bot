package ru.katyshev.kafka.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.katyshev.kafka.dto.CommandDTO;
import ru.katyshev.kafka.dto.EventDTO;

@Component
public class KafkaSender {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String KEY = "0";
    private static final String EVENT_TOPIC = "evnt";
    private static final String COMMAND_TOPIC = "command";

    @Autowired
    public KafkaSender(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(EventDTO eventDTO) {
        kafkaTemplate.send(EVENT_TOPIC, KEY, eventDTO);
    }

    public void sendCommand(CommandDTO commandDTO) {
        kafkaTemplate.send(COMMAND_TOPIC, KEY, commandDTO);
    }
}
