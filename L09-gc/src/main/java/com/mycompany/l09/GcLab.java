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
 * OpenJDK builds from Oracle disabled Shenandoah:
 * https://developers.redhat.com/blog/2019/04/19/not-all-openjdk-12-builds-include-shenandoah-heres-why/
 * <p>
 * -Xms2048m
 * -Xmx2048m
 * -Xlog:gc=debug:file=L09-gc/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
 * -XX:+HeapDumpOnOutOfMemoryError
 * -XX:HeapDumpPath=L09-gc/logs/dump
 * <p>
 * -XX:+UseSerialGC
 * -XX:+UseParallelGC
 * -XX:+UseG1GC
 * -XX:+UnlockExperimentalVMOptions -XX:+UseShenandoahGC
 */
public class GcLab {

    private static boolean isAppRunning = true;

    private static int numYoungGcPerMin;
    private static int numOldGcPerMin;
    private static long timeYoungGcPerMin;
    private static long timeOlgGcPerMin;

    private static boolean isShenandoahUsed = false;
    private static int numShenandoahGcPerMin;
    private static long timeShenandoahGcPerMin;

    public static void main(String[] args) {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        switchOnGcMonitoring();
        long beginTime = System.currentTimeMillis();

        monitorLogging();
        run();
        //runToOutOfMemory();

        isAppRunning = false;
        System.out.println("Application worked: " + ((System.currentTimeMillis() - beginTime) / 1000) + "s");
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
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    //System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");
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

    private static void run() {
        int loopCounter = 1_000;
        int numAdd = 5_000_000;

        for (int i = 0; i < loopCounter; i++) {
            String[] array = new String[numAdd];
            for (int a = 0; a < numAdd; a++) {
                array[a] = new String(new char[0]);
            }
        }
    }

    private static void runToOutOfMemory() {
        int numAdd = 50_000;
        int numDelete = 25_000;

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

    private static void monitorLogging() {
        new Thread(() -> {
            while (isAppRunning) {
                try {
                    Thread.sleep(60_000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (isShenandoahUsed) {
                    System.out.println("\nNumber of GC: " + numShenandoahGcPerMin +
                            "\nTime for GC: " + timeShenandoahGcPerMin + "ms");
                    numShenandoahGcPerMin = 0;
                    timeShenandoahGcPerMin = 0;
                } else {
                    System.out.println("\nNumber of Young GC: " + numYoungGcPerMin +
                            "\nNumber of Old GC: " + numOldGcPerMin +
                            "\nTime for young GC: " + timeYoungGcPerMin + "ms" +
                            "\nTime for old GC: " + timeOlgGcPerMin + "ms" +
                            "\nOverall GC time: " + (timeYoungGcPerMin + timeOlgGcPerMin) + "ms");
                    numYoungGcPerMin = numOldGcPerMin = 0;
                    timeYoungGcPerMin = timeOlgGcPerMin = 0;
                }

            }
        }).start();
    }
}
