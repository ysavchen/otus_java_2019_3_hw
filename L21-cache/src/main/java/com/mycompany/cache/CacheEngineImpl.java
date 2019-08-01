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

    private CacheEngineImpl(Builder builder) {
        this.maxElements = builder.maxElements;
        this.lifeTimeMs = builder.lifeTimeMs > 0 ? builder.lifeTimeMs : 0;
        this.idleTimeMs = builder.idleTimeMs > 0 ? builder.idleTimeMs : 0;
        this.isEternal = builder.lifeTimeMs == 0 && builder.idleTimeMs == 0 || builder.isEternal;
        for (EventType eventType : EventType.values()) {
            listenersMap.put(eventType, new ArrayList<>());
        }
    }

    public static class Builder {
        private int maxElements;
        private long lifeTimeMs;
        private long idleTimeMs;
        private boolean isEternal;

        public Builder(int maxElements) {
            this.maxElements = maxElements;
        }

        public Builder withLifeTime(long lifeTimeMs) {
            this.lifeTimeMs = lifeTimeMs;
            return this;
        }

        public Builder withIdleTime(long idleTimeMs) {
            this.idleTimeMs = idleTimeMs;
            return this;
        }

        public Builder isEternal(boolean isEternal) {
            this.isEternal = isEternal;
            return this;
        }

        public <K, V> CacheEngineImpl<K, V> build() {
            return new CacheEngineImpl<>(this);
        }
    }

    @Override
    public void put(K key, V value) {
        if (key == null || value == null) {
            return;
        }
        if (elements.size() == maxElements) {
            K leastAccessKey = elements.keySet().stream()
                    .min(Comparator.comparingLong(elementKey -> {
                        var reference = elements.get(elementKey);
                        var element = reference.get();
                        if (element != null) {
                            return element.getLastAccessTime();
                        }
                        //to remove a softReference with null element from the 'elements' map
                        return Long.MIN_VALUE;
                    })).orElse(null);

            elements.remove(leastAccessKey);
        }

        Element<K, V> element = new Element<>(key, value);
        elements.put(key, new SoftReference<>(element));
        notify(key, value, EventType.PUT);

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

    private void notify(K key, V value, EventType eventType) {
        try {
            listenersMap.get(eventType)
                    .forEach(listener -> listener.notify(key, value, eventType));
        } catch (Exception ex) {
            System.out.println("Exception in notify: " + ex.toString());
        }
    }

    @Override
    public void remove(K key) {
        SoftReference<Element<K, V>> reference = elements.get(key);
        if (reference != null) {
            Element<K, V> element = reference.get();
            if (element != null) {
                notify(key, element.getValue(), EventType.REMOVE);
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
                V value = element.getValue();
                notify(key, value, EventType.GET);
                return value;
            } else {
                misses++;
                notify(key, null, EventType.GET);
                elements.remove(key);
                return null;
            }
        }
        misses++;
        notify(key, null, EventType.GET);
        return null;
    }

    @Override
    public int getSize() {
        return elements.size();
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
    public void addListener(CacheListener<K, V> listener, EventType eventType, EventType... eventTypes) {
        listenersMap.get(eventType).add(listener);
        for (var type : eventTypes) {
            listenersMap.get(type).add(listener);
        }
    }

    @Override
    public void removeListener(CacheListener<K, V> listener, EventType eventType, EventType... eventTypes) {
        listenersMap.get(eventType).remove(listener);
        for (var type : eventTypes) {
            listenersMap.get(type).remove(listener);
        }
    }
}
