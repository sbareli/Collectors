package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AnalyticMgr {

    // API
    public void addToCollector(String collectorName, String collectorItem)
    {
        AnalyticCollector analyticCollector = collectors.get(collectorName);

        // prevent missing a counter bit on multiple concurrent initialization
        if (analyticCollector == null)
        {

            synchronized (this) {

                analyticCollector = collectors.get(collectorName);

                if (analyticCollector == null) {

                    analyticCollector = new AnalyticCollector() {

                        @Override
                        public String whoAmI() {
                            return collectorName;
                        }

                    };

                    collectors.putIfAbsent(collectorName, analyticCollector);
                }
            }
        }

        analyticCollector.increment(collectorItem);
    }

    public String calculate()
    {
        StringBuilder sb = new StringBuilder();

        for (AnalyticCollector analyticCollector : collectors.values()) {

            for (Enumeration<String> collectorItems = analyticCollector.getItems(); collectorItems.hasMoreElements(); )
            {
                String output = analyticCollector.calculate(collectorItems.nextElement());

                sb.append(output).append("\n");
            }

        }

        return sb.toString();
    }

    // private
    private ConcurrentHashMap<String, AnalyticCollector> collectors = new ConcurrentHashMap<>();

}
