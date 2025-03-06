package com.vince.echotask.controller;

import com.vince.echotask.models.*;
import com.vince.echotask.service.EchoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class EchoTaskController {

    @Autowired
    EchoTaskService echoTaskService;

    @PostMapping("/detect-intent")
    ResponseEntity<ParsedIntent> detectIntent(@RequestBody IntentRequest request) throws IOException, IllegalAccessException {
        log.info(request.toString());

        ParsedIntent parsedIntent = echoTaskService.processIntent(request);
        return new ResponseEntity<>(parsedIntent, HttpStatus.OK);
    }

    @PostMapping("/create-task")
    ResponseEntity<CreateTaskResponse> createTask(@RequestBody TaskRequest request) {
        log.info("create task request: {}", request.toString());

        CreateTaskResponse response = echoTaskService.saveTask(request.getDescription());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-task")
    ResponseEntity<CreateTaskResponse> deleteTask(@RequestBody DeleteTaskRequest request) throws IllegalAccessException {
        log.info("Delete task request: {}", request);

        CreateTaskResponse response = echoTaskService.deleteTask(null, request.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
