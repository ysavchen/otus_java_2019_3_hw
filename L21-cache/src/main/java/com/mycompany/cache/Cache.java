package com.mycompany.cache;

/**
 * Свой cache engine
 * Напишите свой cache engine с soft references.
 * Добавьте кэширование в DBService из задания про Hibernate ORM
 */
public interface Cache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(CacheListener listener);

    void removeListener(CacheListener listener);
}
