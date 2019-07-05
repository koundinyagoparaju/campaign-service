package no.kobler.core;

import org.apache.commons.collections4.queue.CircularFifoQueue;

class RateLimiter {
    private long interval;
    private CircularFifoQueue<Long> lastNTimeStamps;

    RateLimiter(int limit, long intervalInSeconds) {
        this.interval = intervalInSeconds * 1000;
        lastNTimeStamps = new CircularFifoQueue<>(limit);
    }

    synchronized boolean canAdd() {
        long now = System.currentTimeMillis();
        lastNTimeStamps.removeIf(timestamp -> now - timestamp > interval);
        if(lastNTimeStamps.isEmpty() || (!lastNTimeStamps.isAtFullCapacity() && now - lastNTimeStamps.peek() < interval)) {
            lastNTimeStamps.add(now);
            return true;
        } else {
            return false;
        }
    }
}
