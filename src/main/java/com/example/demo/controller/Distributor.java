package com.example.demo.controller;

import com.example.demo.service.AnalyticMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/process")
public class Distributor {

    static Logger logger = LoggerFactory.getLogger(Distributor.class);

    @Autowired
    private AnalyticMgr analyticMgr;

    @GetMapping("/ping")
    public String ping() {
        return "PONG";
    }

    @GetMapping("/calculate")
    public String calculate() {
        String output = analyticMgr.calculate();
        return output;
    }

    // MediaType.APPLICATION_JSON_VALUE
    @RequestMapping(value = "/submit", method= RequestMethod.PUT, consumes = MediaType.TEXT_PLAIN_VALUE)
    public Integer updateTriangle(@RequestBody String message) {

        String[] keyValues = message.split("[|]");

        Integer status = keyValues.length;

        for (String item : keyValues) {
            String keyValue[] = item.split("=",2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                logger.info("Collector={}, Item={}", key, value);

                analyticMgr.addToCollector(key,value);
            }
        }

        return status;
    }


}
