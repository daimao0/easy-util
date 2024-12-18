package com.easy.util;

/**
 * id utils
 *
 * @author daimao
 * @date 2024-12-17 16:00
 */
public class IdGenerator {
    private static final SnowFlake ID_WORKER;
    // 需要根据实际情况配置
    private static final long WORKER_ID = 1;
    // 需要根据实际情况配置
    private static final long DATACENTER_ID = 1;

    static {
        try {
            ID_WORKER = new SnowFlake(WORKER_ID, DATACENTER_ID);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to initialize SnowflakeIdWorker", e);
        }
    }

    private IdGenerator() {
    }

    public static synchronized long generateId() {
        return ID_WORKER.nextId();
    }

}
