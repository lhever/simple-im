package com.lhever.common.core.config;

import java.util.*;

public class OrderedProperties extends Properties {

    private static final long serialVersionUID = 4710927773256743817L;

    private final LinkedHashSet<Object> keys = new LinkedHashSet<Object>();

    public OrderedProperties() {
    }

    public OrderedProperties(Properties defaults) {
        super(defaults);
    }


    @Override
    public synchronized Object put(Object key, Object value) {
        keys.add(key);
        return super.put(key, value);
    }


    @Override
    public synchronized Object putIfAbsent(Object key, Object value) {
        Object o = super.putIfAbsent(key, value);
        if (o == null) {
            keys.add(key);
        }
        return o;
    }

    @Override
    public synchronized Object remove(Object key) {
        keys.remove(key);
        return super.remove(key);
    }

    @Override
    public synchronized boolean remove(Object key, Object value) {
        keys.remove(key);
        return super.remove(key, value);
    }

    @Override
    public Set<Object> keySet() {
        return keys;
    }

    @Override
    public Enumeration<Object> keys() {
        return Collections.<Object> enumeration(keys);
    }


    @Override
    public Set<String> stringPropertyNames() {
        Set<String> set = new LinkedHashSet<String>();

        for (Object key : this.keys) {
            set.add((String) key);
        }

        return set;
    }
}
