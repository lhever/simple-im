package com.lhever.common.core.support.lb;

import com.lhever.common.core.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class RoundRobinStrategy<T> implements ChooseStrategy<T> {

    private static Logger log = LoggerFactory.getLogger(RoundRobinStrategy.class);

    private AtomicInteger nextServerCyclicCounter;

    public RoundRobinStrategy() {
        this.nextServerCyclicCounter = new AtomicInteger(0);
    }

    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next)) {
                return next;
            }
        }
    }


    @Override
    public T choose(List<T> servers, Object key) {
        if (CollectionUtils.isEmpty(servers)) {
            return null;
        }

        int serverCount = servers.size();

        int nextServerIndex = incrementAndGetModulo(serverCount);
        return servers.get(nextServerIndex);
    }

    @Override
    public T choose(Supplier<List<T>> holder, Object key) {
        if (holder == null) {
            return null;
        }

        T server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<T> allServers = holder.get();
            if (allServers == null || allServers.size() == 0) {
                log.warn("no service available");
                Thread.yield();
                continue;
            }

            int serverCount = allServers.size();

            int nextServerIndex = incrementAndGetModulo(serverCount);
            server = allServers.get(nextServerIndex);
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries");
        }
        return server;
    }


}





