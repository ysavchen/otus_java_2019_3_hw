package com.mycompany.mutiprocess.frontend.service;

import com.mycompany.mutiprocess.ms_client.ClientType;
import com.mycompany.mutiprocess.ms_client.Message;
import com.mycompany.mutiprocess.ms_client.MessageType;
import com.mycompany.mutiprocess.ms_client.MsClient;
import com.mycompany.mutiprocess.ms_client.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class FrontendServiceImpl implements FrontendService {

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final ClientType databaseClient;

    public FrontendServiceImpl(MsClient msClient, ClientType databaseClient) {
        this.msClient = msClient;
        this.databaseClient = databaseClient;

        Message registerMsg = msClient.produceMessage(null, null, MessageType.REGISTER_CLIENT);
        msClient.sendMessage(registerMsg);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            logger.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }

    @Override
    public void storeUser(User user, Consumer<String> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseClient, user, MessageType.STORE_USER);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getAllUsers(Consumer<List<User>> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseClient, null, MessageType.ALL_USERS_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
