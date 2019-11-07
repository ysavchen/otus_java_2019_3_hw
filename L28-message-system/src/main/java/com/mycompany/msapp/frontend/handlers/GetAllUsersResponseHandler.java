package com.mycompany.msapp.frontend.handlers;

import com.mycompany.msapp.common.Serializers;
import com.mycompany.msapp.domain.User;
import com.mycompany.msapp.frontend.FrontendService;
import com.mycompany.msapp.messageSystem.Message;
import com.mycompany.msapp.messageSystem.RequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class GetAllUsersResponseHandler implements RequestHandler {

    private final FrontendService frontendService;

    public GetAllUsersResponseHandler(FrontendService frontendService) {

        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            List<User> users = Serializers.deserialize(msg.getPayload(), List.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            frontendService.takeConsumer(sourceMessageId, List.class).ifPresent(consumer -> consumer.accept(users));

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
