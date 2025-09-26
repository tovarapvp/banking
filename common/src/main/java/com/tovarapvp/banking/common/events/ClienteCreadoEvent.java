package com.tovarapvp.banking.common.events;

import com.tovarapvp.banking.common.events.model.ClienteCreadoPayload;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public class ClienteCreadoEvent extends BaseEvent<ClienteCreadoPayload> {
    public ClienteCreadoEvent(ClienteCreadoPayload payload) {
        super(UUID.randomUUID(), LocalDateTime.now(), payload);
    }
}
