package com.mycompany.cache;

class Element<K, V> {

    private final K key;
    private final V value;
    private final long creationTime;
    private long lastAccessTime;

    Element(K key, V value) {
        this.key = key;
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    K getKey() {
        return key;
    }

    V getValue() {
        return value;
    }

    long getCreationTime() {
        return creationTime;
    }

    long getLastAccessTime() {
        return lastAccessTime;
    }

    void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
