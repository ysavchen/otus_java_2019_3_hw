package com.mycompany.msapp.messageSystem;

import java.util.Optional;

public interface MessageHandler {
    Optional<Message> handle(Message msg);
}
