package com.tovarapvp.banking.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public abstract class BaseEvent<T> implements Serializable {
    private UUID eventId;
    private LocalDateTime eventDate;
    private T data;

       public BaseEvent(UUID id, LocalDateTime date, T payload) {
        this.eventId = id;
        this.eventDate = date;
        this.data = payload;
    }
}
