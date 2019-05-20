package com.mycompany.l09;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.apache.commons.lang3.ArrayUtils;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * Liberica JDK version 12.0.1
 * OpenJDK builds from Oracle does not support Shenandoah:
 * https://developers.redhat.com/blog/2019/04/19/not-all-openjdk-12-builds-include-shenandoah-heres-why/
 * <p>
 * -Xms512m
 * -Xmx512m
 * -Xlog:gc=debug:file=L09-gc/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
 * -XX:+HeapDumpOnOutOfMemoryError
 * -XX:HeapDumpPath=L09-gc/logs/dump
 * <p>
 * Garbage Collectors:
 * -XX:+UseSerialGC
 * -XX:+UseParallelGC
 * -XX:+UseG1GC
 * -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
 */
public class GcLab {

    private static int numYoungGcPerMin;
    private static int numOldGcPerMin;
    private static long timeYoungGcPerMin;
    private static long timeOlgGcPerMin;

    //shenandoah does not distinguish young and old generations
    private static boolean isShenandoahUsed = false;
    private static int numShenandoahGcPerMin;
    private static long timeShenandoahGcPerMin;

    public static void main(String[] args) {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnGcMonitoring();
        long beginTime = System.currentTimeMillis();

        Thread thread = getLoggingThread();
        try {
            thread.start();
            runToOutOfMemory();
        } finally {
            thread.interrupt();
            long appWorked = System.currentTimeMillis() - beginTime;
            System.out.println("\nApplication worked: " + (appWorked / 1000) + "s");
        }
    }

    private static void switchOnGcMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    long duration = info.getGcInfo().getDuration();

                    if (gcAction.contains("minor")) {
                        numYoungGcPerMin++;
                        timeYoungGcPerMin += duration;
                    }
                    if (gcAction.contains("major")) {
                        numOldGcPerMin++;
                        timeOlgGcPerMin += duration;
                    }
                    if (gcName.contains("Shenandoah")) {
                        isShenandoahUsed = true;
                        numShenandoahGcPerMin++;
                        timeShenandoahGcPerMin += duration;
                    }
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private static void runToOutOfMemory() {
        int numAdd = 20_000;
        int numDelete = 10_000;

        String[] outer = new String[0];
        while (true) {
            String[] inner = new String[numAdd];
            for (int a = 0; a < numAdd; a++) {
                inner[a] = new String(new char[0]);
            }

            for (int d = 0; d < numDelete; d++) {
                inner[d] = null;
            }
            outer = ArrayUtils.addAll(outer, inner);
        }
    }

    private static Thread getLoggingThread() {
        return new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60_000);
                } catch (Throwable t) {
                    throwAsUncheckedException(t);
                }

                if (isShenandoahUsed) {
                    System.out.println("\nNumber of GC: " + numShenandoahGcPerMin +
                            "\nTime for GC (per min): " + timeShenandoahGcPerMin + "ms");
                    numShenandoahGcPerMin = 0;
                    timeShenandoahGcPerMin = 0;
                } else {
                    System.out.println("\nNumber of Young GC: " + numYoungGcPerMin +
                            "\nNumber of Old GC: " + numOldGcPerMin +
                            "\nTime for young GC (per min): " + timeYoungGcPerMin + "ms" +
                            "\nTime for old GC (per min): " + timeOlgGcPerMin + "ms" +
                            "\nOverall GC time (per min): " + (timeYoungGcPerMin + timeOlgGcPerMin) + "ms");
                    numYoungGcPerMin = numOldGcPerMin = 0;
                    timeYoungGcPerMin = timeOlgGcPerMin = 0;
                }

            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> RuntimeException throwAsUncheckedException(Throwable t) throws T {
        throw (T) t;
    }
}
