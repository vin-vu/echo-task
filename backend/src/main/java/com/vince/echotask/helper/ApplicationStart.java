package com.vince.echotask.helper;

import com.vince.echotask.nlp.PhraseParser;
import com.vince.echotask.nlp.Tokenizer;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ApplicationStart {

    @Autowired
    Tokenizer tokenizer;

    @Autowired
    PhraseParser parser;


    @EventListener(ApplicationReadyEvent.class)
    private void run() throws IOException {

        String utterance = "Add item complete oil change on GR86 this weekend";
        String utterance2 = "Add item do oil change on GR86 this weekend";
        String utterance3 = "Add item doctor appointment is tomorrow";
        String utterance4 = "Add item tire center appointment is at night";
        String utterance5 = "Add task buy new tires from costco";

//        SemanticGraph dependencyParse = parser.createDependencyParseTree(utterance2);
//        String taskDescription = parser.extractDescription(dependencyParse);

        SemanticGraph dependencyParse2 = parser.createDependencyParseTree(utterance2);
        String taskDescription2 = parser.extractDescription(dependencyParse2);
//
//        SemanticGraph dependencyParse3 = parser.createDependencyParseTree(utterance3);
//        String taskDescription3 = parser.extractDescription(dependencyParse3);
//
//        SemanticGraph dependencyParse4 = parser.createDependencyParseTree(utterance4);
//        String taskDescription4 = parser.extractDescription(dependencyParse4);

        SemanticGraph dependencyParse5 = parser.createDependencyParseTree(utterance5);
        String taskDescription5 = parser.extractDescription(dependencyParse5);


//        intentCategorizer.trainModel();

        // below is test on start up
//        String phrase = "add task get my new Michelin Pilot Sport 5 tires installed at Costco because my stock tires need to be replaced";
//        String[] phraseTokens = tokenizer.tokenizeText(phrase);
//        log.info("phrase tokens: {}", (Object) phraseTokens);
//
//        SortedMap<Double, Set<String>> sortedScoreMap = intentCategorizer.categorizeIntent(phraseTokens);
//        log.info("sortedScoreMap: {}", sortedScoreMap);
//
//        Double highestScore = sortedScoreMap.lastKey();
//        Set<String> bestIntents = sortedScoreMap.get(highestScore); // not handling if < 1 intent share same probability and therefore in same Set
//        String bestIntent = bestIntents.iterator().next();
//
//
//        log.info("bestIntents: {}", sortedScoreMap.get(highestScore));
//        log.info("bestIntent: {}", bestIntent);
    }
}
