package com.tovarapvp.banking.common.events;

import com.tovarapvp.banking.common.events.model.MovimientoCreadoPayload;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public class MovimientoCreadoEvent extends BaseEvent<MovimientoCreadoPayload> {
    public MovimientoCreadoEvent(MovimientoCreadoPayload payload) {
        super(UUID.randomUUID(), LocalDateTime.now(), payload);
    }
}
