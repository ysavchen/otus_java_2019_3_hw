package com.mycompany.mutiprocess.ms_client;

import java.util.Optional;
import java.util.UUID;

public interface MessageHandler {

    Optional<Message> handle(Message msg, UUID clientId);

}
