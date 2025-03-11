package com.vince.echotask.helper;

import com.vince.echotask.nlp.IntentCategorizer;
import com.vince.echotask.nlp.PhraseParser;
import com.vince.echotask.nlp.Tokenizer;
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

    @Autowired
    IntentCategorizer intentCategorizer;

    @EventListener(ApplicationReadyEvent.class)
    private void run() throws IOException {

        // root is Add/VB
        String utterance = "Add complete oil change on GR86 this weekend";

        // root is Night/NN
        String utterance4 = "Add tire center appointment is at night";

        // root is Buy/VB
        String utterance6 = "Add go to Costco to buy gas";
        String utterance7 = "Add buy new tires from costco";

        // root is get/VBP
        String utterance8 = "Insert buy gas tomorrow";

        // root is go/NN
        String utterance9 = "Delete go to Costco";

//        SemanticGraph dependencyParse = parser.createDependencyParseTree(utterance);
//        String taskDescription = parser.extractDescription(dependencyParse);

//        SemanticGraph dependencyParse4 = parser.createDependencyParseTree(utterance4);
//        String taskDescription4 = parser.extractDescription(dependencyParse4);

//        SemanticGraph dependencyParse6 = parser.createDependencyParseTree(utterance6);
//        String taskDescription6 = parser.extractDescription(dependencyParse6);
//
//        SemanticGraph dependencyParse7 = parser.createDependencyParseTree(utterance7);
//        String taskDescription7 = parser.extractDescription(dependencyParse7);
//
//        SemanticGraph dependencyParse8 = parser.createDependencyParseTree(utterance8);
//        String taskDescription8 = parser.extractDescription(dependencyParse8);

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
