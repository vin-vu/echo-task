package com.vince.echotask.service;

import com.vince.echotask.pojo.IntentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EchoTaskService {

    public void processIntent(IntentRequest request) {

        log.info("process intent: {}", request.toString());

        // Step 1: Call NLP service to analyze intent

        // Step 2: Store request and intent in Postgres
    }
}
