package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public abstract class AnalyticCollector {

    static Logger logger = LoggerFactory.getLogger(AnalyticCollector.class);

    abstract public String whoAmI();

    public Long increment(String collectorItem)
    {
        AtomicLong counter = collectorItems.get(collectorItem);

        // prevent missing a counter bit on multiple concurrent initialization
        if (counter == null)
        {

            synchronized (this) {

                counter = collectorItems.get(collectorItem);

                if (counter == null) {
                    counter = new AtomicLong(0);
                    collectorItems.putIfAbsent(collectorItem, counter);
                }
            }
        }

        Long val = counter.getAndIncrement();

        return val;
    }

    public Enumeration<String> getItems() {
        return collectorItems.keys();
    }

    public String calculate(String collectorItem)
    {
        Double percent = 0.0;

        AtomicLong counter = collectorItems.get(collectorItem);

        if (counter != null) {

            Long sum = 0L;
            for (AtomicLong item : collectorItems.values()) {
                sum += item.get();
            }

            percent = counter.doubleValue() / sum * 100.0;
        }

        logger.info("Collector:{}, Item:{}, Percent:{}", whoAmI(), collectorItem, percent);

        return String.format("Collector:%s, Item:%s, Percent:%f", whoAmI(), collectorItem, percent);
    }

    private ConcurrentHashMap<String, AtomicLong> collectorItems = new ConcurrentHashMap<>();
}
