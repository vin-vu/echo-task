package com.vince.echotask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vince.echotask.models.*;
import com.vince.echotask.nlp.depparser.DependencyParser;
import com.vince.echotask.nlp.intentcategorizer.IntentCategorizer;
import com.vince.echotask.nlp.intentcategorizer.Tokenizer;
import com.vince.echotask.repository.EchoTaskRepository;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class EchoTaskService {

    @Autowired
    Tokenizer tokenizer;

    @Autowired
    IntentCategorizer intentCategorizer;

    @Autowired
    DependencyParser dependencyParser;

    @Autowired
    EchoTaskRepository repository;

    @Autowired
    ObjectMapper mapper;

    public ParsedIntent processIntent(IntentRequest request) throws IllegalAccessException, IOException {

        log.info("process intent: {}", request.toString());
        String[] lemmatizedTokens = tokenizer.getTranscriptTokens(request.getTranscript());

        SortedMap<Double, Set<String>> sortedScoreMap = intentCategorizer.categorizeIntent(lemmatizedTokens);
        log.info("sortedScoreMap: {}", sortedScoreMap);

        Double highestScore = sortedScoreMap.lastKey();
        Set<String> bestIntents = sortedScoreMap.get(highestScore); // not handling if more than 1 intent - would share same probability and therefore in same Set
        Intent intent = Intent.valueOf(bestIntents.iterator().next());

        SemanticGraph dependencyParse = dependencyParser.createDependencyParseTree(request.getTranscript());
        String taskDescription = dependencyParser.extractDescription(dependencyParse);

        TaskSummary taskSummary;
        if (Objects.equals(intent, Intent.ADD_TASK)) {
            taskSummary = saveTask(taskDescription);
        } else if (Objects.equals(intent, Intent.DELETE_TASK)) {
            taskSummary = deleteTask(taskDescription, null);
        } else if (Objects.equals(intent, Intent.COMPLETED_TASK)) {
            taskSummary = updateTaskStatus(null, false, taskDescription);
        } else {
            taskSummary = new TaskSummary(UUID.randomUUID(), "unknown to be implemented", false);
        }
        return new ParsedIntent(taskSummary.getId(), intent, taskDescription, taskSummary.isCompleted());
    }

    public TaskSummary saveTask(String description) throws JsonProcessingException {
        Task task = new Task();
        task.setDescription(description);
        task.setCompleted(false);
        Task savedTask = repository.save(task);
        log.info("Saved task : {}", mapper.writeValueAsString(savedTask));
        return new TaskSummary(task.getId(), description, task.isCompleted());
    }

    public TaskSummary updateTaskStatus(UUID id, boolean completedStatus, String description) {

        id = resolveTaskId(id, description);

        Task task = repository.updateTaskStatus(completedStatus, id);
        if (task == null) {
            log.warn("Failed to update Task ID:{} to be status:{}", id, completedStatus);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task ID not found or update failed");
        }

        String message = String.format("Updated task ID: %s to be status: %s", id, completedStatus);
        log.info(message);
        return new TaskSummary(id, description, completedStatus);
    }

    public TaskSummary deleteTask(String description, String id) throws IllegalAccessException {
        Task task;
        if (id != null) {
            task = repository.findById(UUID.fromString(id)).orElseThrow(() -> new RuntimeException("Task not found with given ID"));
        } else if (description != null) {
            task = repository.findBestMatch(description);
        } else {
            throw new IllegalAccessException("Either ID or Description must be provided");
        }
        log.info("Deleted task: {}", task);
        repository.deleteById(task.getId());
        return new TaskSummary(task.getId(), description, task.isCompleted());
    }

    public List<TaskSummary> getAllTasks() throws JsonProcessingException {
        List<TaskSummary> taskSummaries = repository.getAllTaskSummaries();
        log.info("Task summaries: {}", mapper.writeValueAsString(taskSummaries));
        return taskSummaries;
    }

    private UUID resolveTaskId(UUID id, String description) {
        if (id != null) {
            return id;
        }

        if (description == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task ID or description required");
        }

        Task task = repository.findBestMatch(description);
        return task.getId();
    }
}
