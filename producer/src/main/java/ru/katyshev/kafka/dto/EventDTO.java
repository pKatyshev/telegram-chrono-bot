package ru.katyshev.kafka.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private long chatId;
    private String event;
}
