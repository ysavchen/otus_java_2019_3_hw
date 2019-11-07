package com.mycompany.msapp.messageSystem;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
