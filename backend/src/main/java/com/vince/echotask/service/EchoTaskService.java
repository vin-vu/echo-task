package com.vince.echotask.service;

import com.vince.echotask.models.domain.Intent;
import com.vince.echotask.models.dto.request.IntentRequest;
import com.vince.echotask.models.dto.response.IntentResolution;
import com.vince.echotask.models.dto.response.ParsedIntent;
import com.vince.echotask.models.dto.response.TaskSummary;
import com.vince.echotask.nlp.intent.IntentService;
import com.vince.echotask.nlp.task.TaskFinderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Service
public class EchoTaskService {

    private final IntentService intentService;
    private final TaskFinderService taskFinderService;
    private final TaskActionService taskActionService;

    public EchoTaskService(IntentService intentService, TaskFinderService taskFinderService,
                           TaskActionService taskActionService) {
        this.intentService = intentService;
        this.taskFinderService = taskFinderService;
        this.taskActionService = taskActionService;
    }

    public ParsedIntent processIntent(IntentRequest request) {
        String transcript = request.getTranscript();

        IntentResolution intentResolution = intentService.resolveIntent(transcript);
        log.info("intentResolution: {}", intentResolution);
        validateIntent(intentResolution.intent());

        String task = taskFinderService.extractTask(transcript);
        log.info("extracted task: {}", task);
        validateExtractedTask(task);

        TaskSummary taskSummary = handleTaskIntent(intentResolution.intent(), task);

        return new ParsedIntent(
                taskSummary.getId(),
                intentResolution.intent(),
                taskSummary.getDescription(),
                taskSummary.isCompleted(),
                intentResolution.rankedScores()
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

    private void validateExtractedTask(String task) {
        if (Objects.equals(task, "")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Could not extract task. Please try again."
            );
        }
    }

    private TaskSummary handleTaskIntent(Intent intent, String taskDescription) {
        return switch (intent) {
            case ADD_TASK -> taskActionService.saveTask(taskDescription);
            case DELETE_TASK -> taskActionService.deleteTask(null, taskDescription);
            case COMPLETE_TASK -> taskActionService.updateTaskStatus(null, true, taskDescription);
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported intent: " + intent
            );
        };
    }

}