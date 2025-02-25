package com.vince.echotask.helper;

import com.vince.echotask.nlp.IntentCategorizer;
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
    IntentCategorizer intentCategorizer;

    @EventListener(ApplicationReadyEvent.class)
    private void run() throws IOException {


        tokenizer.loadStopWords();
//        intentCategorizer.trainModel();

        String phrase = "add task get my new Michelin Pilot Sport 5 tires installed at Costco because my stock tires need to be replaced";
        String[] phraseTokens = tokenizer.tokenizeText(phrase);
        log.info("phrase tokens: {}", (Object) phraseTokens);

        String categoryResults = intentCategorizer.categorizeIntent(phraseTokens);
        log.info("category results: {}", categoryResults);
    }
}
