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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

@Slf4j
@Service
public class EchoTaskService {

    private final Tokenizer tokenizer;
    private final IntentCategorizer intentCategorizer;
    private final DependencyParser dependencyParser;
    private final EchoTaskRepository repository;
    private final ObjectMapper mapper;

    public EchoTaskService(Tokenizer tokenizer, IntentCategorizer intentCategorizer,
                           DependencyParser dependencyParser, EchoTaskRepository repository, ObjectMapper mapper) {
        this.tokenizer = tokenizer;
        this.intentCategorizer = intentCategorizer;
        this.dependencyParser = dependencyParser;
        this.repository = repository;
        this.mapper = mapper;
    }


    public ParsedIntent processIntent(IntentRequest request) throws IllegalAccessException, IOException {
        log.info("process intent: {}", request);

        String transcript = request.getTranscript();
        String[] lemmatizedTokens = tokenizer.getTranscriptTokens(transcript);

        SortedMap<Double, Set<String>> rankedIntentScores = intentCategorizer.categorizeIntent(lemmatizedTokens);
        log.info("sortedScoreMap: {}", rankedIntentScores);

        var rankedScores = intentCategorizer.convertRankedIntentScores(rankedIntentScores);
        Intent intent = intentCategorizer.getBestIntent(rankedIntentScores);
        log.info("best intent: {}", intent);

        validateIntent(intent);

        SemanticGraph dependencyParse = dependencyParser.createDependencyParseTree(transcript);
        String taskDescription = dependencyParser.extractDescription(dependencyParse, intent);
        TaskSummary taskSummary = handleTaskIntent(intent, taskDescription);

        return new ParsedIntent(
                taskSummary.getId(),
                intent,
                taskSummary.getDescription(),
                taskSummary.isCompleted(),
                rankedScores
        );
    }

    private void validateIntent(Intent intent) {
        if (Intent.UNKNOWN.equals(intent)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not determine intent. Please say add, delete, or complete."
            );
        }
    }

    private TaskSummary handleTaskIntent(Intent intent, String taskDescription) throws JsonProcessingException,
            IllegalAccessException {
        return switch (intent) {
            case ADD_TASK -> saveTask(taskDescription);
            case DELETE_TASK -> deleteTask(taskDescription, null);
            case COMPLETE_TASK -> updateTaskStatus(null, true, taskDescription);
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported intent: " + intent
            );
        };
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
            task = repository.findById(UUID.fromString(id)).orElseThrow(() -> new RuntimeException("Task not found " +
                    "with given ID"));
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

        // below is flow for voice command
        Task task = repository.findBestMatch(description);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find task from given voice command");
        } else {
            return task.getId();
        }
    }
}
