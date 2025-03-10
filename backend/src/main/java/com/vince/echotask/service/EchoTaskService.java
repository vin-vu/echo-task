package com.vince.echotask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vince.echotask.models.*;
import com.vince.echotask.nlp.IntentCategorizer;
import com.vince.echotask.nlp.PhraseParser;
import com.vince.echotask.nlp.Tokenizer;
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
    PhraseParser phraseParser;

    @Autowired
    EchoTaskRepository repository;

    private static final ObjectMapper mapper = new ObjectMapper();

    public ParsedIntent processIntent(IntentRequest request) throws IllegalAccessException, IOException {

        log.info("process intent: {}", request.toString());
        String[] lemmatizedTokens = tokenizer.getTranscriptTokens(request.getTranscript());

        SortedMap<Double, Set<String>> sortedScoreMap = intentCategorizer.categorizeIntent(lemmatizedTokens);
        log.info("sortedScoreMap: {}", sortedScoreMap);

        Double highestScore = sortedScoreMap.lastKey();
        Set<String> bestIntents = sortedScoreMap.get(highestScore); // not handling if more than 1 intent - would share same probability and therefore in same Set
        Intent intent = Intent.valueOf(bestIntents.iterator().next());

        SemanticGraph dependencyParse = phraseParser.createDependencyParseTree(request.getTranscript());
        String taskDescription = phraseParser.extractDescription(dependencyParse);

        TaskSummary taskSummary;
        if (Objects.equals(intent, Intent.ADD_TASK)) {
            taskSummary = saveTask(taskDescription);
        } else if (Objects.equals(intent, Intent.DELETE_TASK)) {
            taskSummary = deleteTask(taskDescription, null);
        } else if (Objects.equals(intent, Intent.MARK_DONE)) {
            taskSummary = new TaskSummary(UUID.randomUUID(), "mark done to be implemented", TaskStatus.PENDING);
        } else {
            taskSummary = new TaskSummary(UUID.randomUUID(), "unknown to be implemented", TaskStatus.PENDING);
        }
        return new ParsedIntent(taskSummary.getId(), intent, taskDescription, TaskStatus.PENDING);
    }

    public TaskSummary saveTask(String description) throws JsonProcessingException {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(TaskStatus.PENDING);
        Task savedTask = repository.save(task);
        log.info("Saved task : {}", mapper.writeValueAsString(savedTask));
        return new TaskSummary(task.getId(), description, task.getStatus());
    }

    public UpdateStatusResponse updateTaskStatus(String id, TaskStatus status) {
        int rowsUpdated = repository.updateTaskStatus(status, UUID.fromString(id));
        String message;
        if (rowsUpdated == 1) {
            message = String.format("Updated task ID: %s to be status: %s", id, status);
            log.info("Updating task ID:{} to be status:{}", id, status);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task ID not found or update failed");
        }
        return new UpdateStatusResponse(message);
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
        return new TaskSummary(task.getId(), description, task.getStatus());
    }

    public List<TaskSummary> getAllTasks() throws JsonProcessingException {
        List<TaskSummary> taskSummaries = repository.getAllTaskSummaries();
        log.info("Task summaries: {}", mapper.writeValueAsString(taskSummaries));
        return taskSummaries;
    }
}
