package com.mycompany.cache;

/**
 * Свой cache engine
 * Напишите свой cache engine с soft references.
 * Добавьте кэширование в DBService из задания про Hibernate ORM
 */
public interface CacheEngine<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void dispose();

    int getHitCount();

    int getMissCount();

    void addListener(CacheListener<K, V> listener, EventType... eventTypes);

    void removeListener(CacheListener<K, V> listener, EventType... eventTypes);
}
