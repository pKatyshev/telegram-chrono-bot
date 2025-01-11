package ru.katyshev.kafka.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServerResponseDTO {
    private long chatId;
    private String response;
}
