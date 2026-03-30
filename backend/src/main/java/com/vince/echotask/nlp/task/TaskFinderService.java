package com.vince.echotask.nlp.task;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskFinderService {

    private final TaskFinderModelService taskFinderModelService;

    public TaskFinderService(TaskFinderModelService taskFinderModelService) {
        this.taskFinderModelService = taskFinderModelService;
    }

    public Span[] extractTask(String[] test) {
        return taskFinderModelService.findTask(test);
    }
}
