package com.mycompany.mutiprocess.ms_client;

import java.util.Optional;

public interface MessageHandler {

    Optional<Message> handle(Message msg);

}
