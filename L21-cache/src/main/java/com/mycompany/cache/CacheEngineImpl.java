package com.mycompany.cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private static final int TIME_THRESHOLD_MS = 5;

    private final Map<K, SoftReference<Element<K, V>>> elements = new LinkedHashMap<>();

    private final Timer timer = new Timer();

    private final Map<EventType, List<CacheListener<K, V>>> listenersMap = new HashMap<>();

    /**
     * Counter for cache hits
     */
    private int hits = 0;

    /**
     * Counter for cache misses
     */
    private int misses = 0;

    /**
     * Max number of elements to be stored in CacheEngine.
     */
    private final int maxElements;

    /**
     * Time in ms for an element to live in CacheEngine.
     */
    private final long lifeTimeMs;

    /**
     * Time in ms for an element to live in CacheEngine since the last access.
     */
    private final long idleTimeMs;

    /**
     * Flag to store elements without removal by lifeTime/idleTime.
     */
    private final boolean isEternal;

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
        for (EventType eventType : EventType.values()) {
            listenersMap.put(eventType, new ArrayList<>());
        }
    }

    @Override
    public void put(K key, V value) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        Element<K, V> element = new Element<>(key, value);
        elements.put(key, new SoftReference<>(element));
        listenersMap.get(EventType.PUT)
                .forEach(putListener -> putListener.notify(key, value, EventType.PUT));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    @Override
    public void remove(K key) {
        SoftReference<Element<K, V>> reference = elements.get(key);
        if (reference != null) {
            Element<K, V> element = reference.get();
            if (element != null) {
                listenersMap.get(EventType.REMOVE)
                        .forEach(putListener -> putListener.notify(key, element.getValue(), EventType.REMOVE));
            }

        }
        elements.remove(key);
    }

    @Override
    public V get(K key) {
        SoftReference<Element<K, V>> reference = elements.get(key);
        if (reference != null) {
            Element<K, V> element = reference.get();
            if (element != null) {
                hits++;
                element.setAccessed();
                listenersMap.get(EventType.GET)
                        .forEach(getListener -> getListener.notify(key, element.getValue(), EventType.GET));

                return element.getValue();
            } else {
                misses++;
                elements.remove(key);
                return null;
            }
        }
        misses++;
        return null;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    @Override
    public int getHitCount() {
        return hits;
    }

    @Override
    public int getMissCount() {
        return misses;
    }

    private TimerTask getTimerTask(final K key, Function<Element<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                SoftReference<Element<K, V>> reference = elements.get(key);
                Element<K, V> element = reference.get();
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }

    @Override
    public void addListener(CacheListener<K, V> listener, EventType... eventTypes) {
        for (var eventType : eventTypes) {
            listenersMap.get(eventType).add(listener);
        }
    }

    @Override
    public void removeListener(CacheListener<K, V> listener, EventType... eventTypes) {
        for (var eventType : eventTypes) {
            listenersMap.get(eventType).remove(listener);
        }
    }
}
