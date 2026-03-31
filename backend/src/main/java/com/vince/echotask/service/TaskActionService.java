package com.vince.echotask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vince.echotask.models.domain.Task;
import com.vince.echotask.models.dto.response.TaskSummary;
import com.vince.echotask.repository.EchoTaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class TaskActionService {

    private final EchoTaskRepository repository;
    private final ObjectMapper mapper;

    public TaskActionService(EchoTaskRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    public TaskSummary saveTask(String description) {
        Task task = new Task();
        task.setDescription(description);
        task.setCompleted(false);

        Task savedTask = repository.save(task);
        log.info("Saved task: {}", savedTask);

        return new TaskSummary(savedTask.getId(), savedTask.getDescription(), savedTask.isCompleted());
    }

    public TaskSummary updateTaskStatus(UUID id, boolean completedStatus, String description) {
        UUID resolvedId = resolveTaskId(id, description);

        Task task = repository.updateTaskStatus(completedStatus, resolvedId);
        if (task == null) {
            log.warn("Failed to update Task ID:{} to status:{}", resolvedId, completedStatus);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found or update failed");
        }

        log.info("Updated task ID:{} to status:{}", resolvedId, completedStatus);
        return new TaskSummary(task.getId(), task.getDescription(), task.isCompleted());
    }

    public TaskSummary deleteTask(UUID id, String description) {
        UUID resolvedId = resolveTaskId(id, description);
        Task task = getTaskOrThrow(resolvedId);

        repository.deleteById(resolvedId);
        log.info("Deleted task: {}", task);

        return new TaskSummary(task.getId(), task.getDescription(), task.isCompleted());
    }

    public List<TaskSummary> getAllTasks() throws JsonProcessingException {
        List<TaskSummary> taskSummaries = repository.getAllTaskSummaries();
        log.info("Task summaries: {}", mapper.writeValueAsString(taskSummaries));
        return taskSummaries;
    }

    private UUID resolveTaskId(UUID id, String description) {
        if (id != null) return id;

        if (description == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Task ID or description required"
            );
        }

        Task task = repository.findBestMatch(description);
        if (task == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No matching task found"
            );
        }

        return task.getId();
    }

    private Task getTaskOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found: " + id
                ));
    }
}
