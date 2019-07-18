package com.mycompany.cache;

public class Element<K, V> {

    private final K key;
    private final V value;
    private final long creationTime;
    private long lastAccessTime;

    public Element(K key, V value) {
        this.key = key;
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
