package com.mycompany.mutiprocess.frontend.service;


import com.mycompany.mutiprocess.ms_client.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendService {

    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);

    void getAllUsers(Consumer<List<User>> dataConsumer);

    void storeUser(User user, Consumer<String> dataConsumer);

}
