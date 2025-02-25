package com.vince.echotask.controller;

import com.vince.echotask.pojo.IntentRequest;
import com.vince.echotask.service.EchoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class EchoTaskController {

    @Autowired
    EchoTaskService echoTaskService;

    @PostMapping("/detect-intent")
    ResponseEntity<Map<String, String>> detectIntent(@RequestBody IntentRequest request) throws IOException {
        log.info(request.toString());

        String intent = echoTaskService.processIntent(request);

        Map<String, String> response = new HashMap<>();
        response.put("intent", intent);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
