package ru.katyshev.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSum {
    private String event;
    private long sum;

    @Override
    public String toString() {
        return "EventSum{" +
                "event='" + event + '\'' +
                ", sum=" + sum +
                '}';
    }
}
