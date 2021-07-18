package com.lhever.common.core.support.lb;

import com.lhever.common.core.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class RandomStrategy<T> implements ChooseStrategy<T> {

    private static Logger log = LoggerFactory.getLogger(RandomStrategy.class);


    @Override
    public T choose(List<T> servers, Object key) {
        if (CollectionUtils.isEmpty(servers)) {
            return null;
        }
        int serverCount = servers.size();
        int index = chooseRandomInt(serverCount);
        return servers.get(index);
    }

    /**
     * Randomly choose from all servers
     */
    @Override
    public T choose(Supplier<List<T>> supplier, Object key) {
        if (supplier == null) {
            return null;
        }
        T server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<T> allList = supplier.get();
            if (allList == null || allList.size() == 0) {
                Thread.yield();
                continue;
            }

            int serverCount = allList.size();
            int index = chooseRandomInt(serverCount);
            server = allList.get(index);
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries");
        }

        return server;

    }

    protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }


}
