package com.vince.echotask.nlp.task;

import com.vince.echotask.nlp.TokenizerService;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@Service
public class TaskFinderService {

    private final TokenizerService tokenizerService;
    private final TaskFinderModelService taskFinderModelService;

    public TaskFinderService(TokenizerService tokenizerService, TaskFinderModelService taskFinderModelService) {
        this.tokenizerService = tokenizerService;
        this.taskFinderModelService = taskFinderModelService;
    }

    public String extractTask(String transcript) {
        String[] tokens = tokenizerService.tokenizeAndNormalize(transcript);
        Span[] taskSpans = taskFinderModelService.findTask(tokens);
        return extractTaskFromSpans(tokens, taskSpans);
    }

    private String extractTaskFromSpans(String[] tokens, Span[] taskSpans) {
        if (taskSpans == null || taskSpans.length == 0) {
            return "";
        }

        Span bestSpan = taskSpans[0];
        return String.join(" ", Arrays.copyOfRange(tokens, bestSpan.getStart(), bestSpan.getEnd()));
    }
}
