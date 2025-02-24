package com.vince.echotask.helper;

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

    @EventListener(ApplicationReadyEvent.class)
    private void run() throws IOException {

        String phrase = "add task I need to get a job right now because I want to make money";

        tokenizer.loadStopWords();
        String[] tokens = tokenizer.tokenizeText(phrase);
        log.info("phrase tokens: {}", (Object) tokens);
    }
}
