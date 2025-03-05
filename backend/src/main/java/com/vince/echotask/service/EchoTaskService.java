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
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;

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

    public ParsedIntent processIntent(IntentRequest request) throws IOException {

        log.info("process intent: {}", request.toString());
        String[] lemmatizedTokens = tokenizer.getTranscriptTokens(request.getTranscript());

        SortedMap<Double, Set<String>> sortedScoreMap = intentCategorizer.categorizeIntent(lemmatizedTokens);
        log.info("sortedScoreMap: {}", sortedScoreMap);

        Double highestScore = sortedScoreMap.lastKey();
        Set<String> bestIntents = sortedScoreMap.get(highestScore); // not handling if more than 1 intent - would share same probability and therefore in same Set
        Intent intent = Intent.valueOf(bestIntents.iterator().next());

        SemanticGraph dependencyParse = phraseParser.createDependencyParseTree(request.getTranscript());
        String taskDescription = phraseParser.extractDescription(dependencyParse);

        if (Objects.equals(intent, Intent.ADD_TASK)) {
            saveTask(taskDescription);
        } else if (Objects.equals(intent, Intent.DELETE_TASK)) {
            deleteTask(taskDescription);
        }
        return new ParsedIntent(intent, taskDescription);
    }

    public String saveTask(String description) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(TaskStatus.PENDING);
        Task savedTask = repository.save(task);
        log.info("savedTask : {}", savedTask);
        return savedTask.getId().toString();
    }

    public void deleteTask(String description) {
        Task task = repository.findBestMatch(description);
        repository.deleteById(task.getId());
        log.info("deleting task: {}", task);
    }
}
