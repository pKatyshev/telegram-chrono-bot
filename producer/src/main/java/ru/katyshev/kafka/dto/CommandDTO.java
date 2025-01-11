package ru.katyshev.kafka.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommandDTO {
    private long chatId;
    private String command;
}
