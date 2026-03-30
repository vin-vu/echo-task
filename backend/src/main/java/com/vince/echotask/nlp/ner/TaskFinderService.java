package com.vince.echotask.nlp.ner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskFinderService {

    private final TaskFinderModelService taskFinderModelService;

    public TaskFinderService(TaskFinderModelService taskFinderModelService) {
        this.taskFinderModelService = taskFinderModelService;
    }

    public void extractTask() {
        String[] test = new String[]{
                "create",
                "fix",
                "broken",
                "shift",
                "key",
                "on",
                "keyboard"
        };
        taskFinderModelService.findTask(test);
    }
}
