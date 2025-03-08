package com.vince.echotask.service;

import com.vince.echotask.models.*;
import com.vince.echotask.nlp.IntentCategorizer;
import com.vince.echotask.nlp.PhraseParser;
import com.vince.echotask.nlp.Tokenizer;
import com.vince.echotask.repository.EchoTaskRepository;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            taskSummary = new TaskSummary(UUID.randomUUID(), "mark done to be implemented", "status");
        } else {
            taskSummary = new TaskSummary(UUID.randomUUID(), "unknown to be implemented", "status");
        }
        return new ParsedIntent(taskSummary.getId(), intent, taskDescription);
    }

    public TaskSummary saveTask(String description) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(TaskStatus.PENDING);
        Task savedTask = repository.save(task);
        log.info("Saved task : {}", savedTask);
        return new TaskSummary(task.getId(), description, task.getStatus().toString());
    }

    public void updateTaskStatus(String id, TaskStatus status) {
        int rowsUpdated = repository.updateTaskStatus(status, UUID.fromString(id));
        log.info("number of rows updated: {}", rowsUpdated);
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
        return new TaskSummary(task.getId(), description, task.getStatus().toString());
    }

    public ArrayList<TaskSummary> getAllTasks() {
        ArrayList<TaskSummary> taskSummaries = repository.getAllTaskSummary();
        log.info("Task summaries: {}", taskSummaries.toString());
        return taskSummaries;
    }
}
