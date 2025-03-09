package com.vince.echotask.controller;

import com.vince.echotask.models.*;
import com.vince.echotask.service.EchoTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
public class EchoTaskController {

    @Autowired
    EchoTaskService echoTaskService;

    @PostMapping("/detect-intent")
    ResponseEntity<ParsedIntent> detectIntent(@RequestBody IntentRequest request) throws IOException, IllegalAccessException {
        log.info(request.toString());

        ParsedIntent parsedIntent = echoTaskService.processIntent(request);
        log.info("Parsed Intent response: {}", parsedIntent.toString());
        return new ResponseEntity<>(parsedIntent, HttpStatus.OK);
    }

    @PostMapping("/create-task")
    ResponseEntity<TaskSummary> createTask(@RequestBody TaskRequest request) {
        log.info("create task request: {}", request.toString());

        TaskSummary response = echoTaskService.saveTask(request.getDescription());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-task-status")
    ResponseEntity<UpdateStatusResponse> updateTaskStatus(@RequestBody UpdateStatusRequest request) {
        log.info("update task status request: {}", request.toString());

        UpdateStatusResponse response = echoTaskService.updateTaskStatus(request.getId(), request.getStatus());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-task")
    ResponseEntity<TaskSummary> deleteTask(@RequestBody DeleteTaskRequest request) throws IllegalAccessException {
        log.info("Delete task request: {}", request);

        TaskSummary response = echoTaskService.deleteTask(null, request.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-tasks")
    ResponseEntity<List<TaskSummary>> getTasks() {
        log.info("get all tasks request");
        List<TaskSummary> response = echoTaskService.getAllTasks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
