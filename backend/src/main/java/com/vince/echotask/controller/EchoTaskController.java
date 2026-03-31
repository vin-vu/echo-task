package com.vince.echotask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vince.echotask.models.dto.request.DeleteTaskRequest;
import com.vince.echotask.models.dto.request.IntentRequest;
import com.vince.echotask.models.dto.request.TaskRequest;
import com.vince.echotask.models.dto.request.UpdateStatusRequest;
import com.vince.echotask.models.dto.response.ParsedIntent;
import com.vince.echotask.models.dto.response.TaskSummary;
import com.vince.echotask.service.EchoTaskService;
import com.vince.echotask.service.TaskActionService;
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
    private final TaskActionService taskActionService;
    private final ObjectMapper mapper;

    public EchoTaskController(EchoTaskService echoTaskService, TaskActionService taskActionService,
                              ObjectMapper mapper) {
        this.echoTaskService = echoTaskService;
        this.taskActionService = taskActionService;
        this.mapper = mapper;
    }

    @PostMapping("/detect-intent")
    ResponseEntity<ParsedIntent> detectIntent(@RequestBody IntentRequest request) throws IOException {
        log.info(request.toString());

        ParsedIntent parsedIntent = echoTaskService.processIntent(request);
        log.info("Parsed Intent response: {}", mapper.writeValueAsString(parsedIntent));
        return new ResponseEntity<>(parsedIntent, HttpStatus.OK);
    }

    @PostMapping("/create-task")
    ResponseEntity<TaskSummary> createTask(@RequestBody TaskRequest request) throws JsonProcessingException {
        log.info("create task request: {}", mapper.writeValueAsString(request));

        TaskSummary response = taskActionService.saveTask(request.getDescription());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update-task-status")
    ResponseEntity<TaskSummary> updateTaskStatus(@RequestBody UpdateStatusRequest request) throws JsonProcessingException {
        log.info("update task status request: {}", mapper.writeValueAsString(request));

        TaskSummary response = taskActionService.updateTaskStatus(UUID.fromString(request.getId()),
                request.isCompleted(), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-task")
    ResponseEntity<TaskSummary> deleteTask(@RequestBody DeleteTaskRequest request) throws
            JsonProcessingException {
        log.info("Delete task request: {}", mapper.writeValueAsString(request));

        TaskSummary response = taskActionService.deleteTask(UUID.fromString(request.getId()), null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-tasks")
    ResponseEntity<List<TaskSummary>> getTasks() throws JsonProcessingException {
        log.info("get all tasks request");
        List<TaskSummary> response = taskActionService.getAllTasks();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
