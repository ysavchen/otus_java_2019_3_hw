package com.mycompany.cache;

@FunctionalInterface
public interface CacheListener<K, V> {

    void notify(K key, V value, EventType eventType);
}
