package com.mycompany.msapp.frontend.handlers;

import com.mycompany.msapp.common.Serializers;
import com.mycompany.msapp.frontend.FrontendService;
import com.mycompany.msapp.messageSystem.Message;
import com.mycompany.msapp.messageSystem.MessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class StoreUserResponseHandler implements MessageHandler {

    private final FrontendService frontendService;

    public StoreUserResponseHandler(FrontendService frontendService) {

        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            String infoMessage = Serializers.deserialize(msg.getPayload(), String.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            frontendService.takeConsumer(sourceMessageId, String.class).ifPresent(consumer -> consumer.accept(infoMessage));

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }

}
