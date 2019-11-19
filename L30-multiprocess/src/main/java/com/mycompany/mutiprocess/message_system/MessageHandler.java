package com.mycompany.mutiprocess.message_system;

import java.util.Optional;

public interface MessageHandler {
    Optional<Message> handle(Message msg);
}
