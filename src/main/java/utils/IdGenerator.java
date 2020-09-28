package utils;

import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {
    private static AtomicLong id = new AtomicLong(1L);

    private IdGenerator() {}

    public static long generateId() {
        return id.getAndIncrement();
    }
}
