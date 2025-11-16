package com.vince.echotask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vince.echotask.models.*;
import com.vince.echotask.service.EchoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class EchoTaskController {

    private final EchoTaskService echoTaskService;
    private final ObjectMapper mapper;

    public EchoTaskController(EchoTaskService echoTaskService, ObjectMapper mapper) {
        this.echoTaskService = echoTaskService;
        this.mapper = mapper;
    }


    @PostMapping("/detect-intent")
    ResponseEntity<ParsedIntent> detectIntent(@RequestBody IntentRequest request) throws IOException,
            IllegalAccessException {
        log.info(request.toString());

        ParsedIntent parsedIntent = echoTaskService.processIntent(request);
        log.info("Parsed Intent response: {}", mapper.writeValueAsString(parsedIntent));
        return new ResponseEntity<>(parsedIntent, HttpStatus.OK);
    }

    @PostMapping("/create-task")
    ResponseEntity<TaskSummary> createTask(@RequestBody TaskRequest request) throws JsonProcessingException {
        log.info("create task request: {}", mapper.writeValueAsString(request));

        TaskSummary response = echoTaskService.saveTask(request.getDescription());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-task-status")
    ResponseEntity<TaskSummary> updateTaskStatus(@RequestBody UpdateStatusRequest request) throws JsonProcessingException {
        log.info("update task status request: {}", mapper.writeValueAsString(request));

        TaskSummary response = echoTaskService.updateTaskStatus(UUID.fromString(request.getId()),
                request.isCompleted(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-task")
    ResponseEntity<TaskSummary> deleteTask(@RequestBody DeleteTaskRequest request) throws IllegalAccessException,
            JsonProcessingException {
        log.info("Delete task request: {}", mapper.writeValueAsString(request));

        TaskSummary response = echoTaskService.deleteTask(null, request.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-tasks")
    ResponseEntity<List<TaskSummary>> getTasks() throws JsonProcessingException {
        log.info("get all tasks request");
        List<TaskSummary> response = echoTaskService.getAllTasks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
