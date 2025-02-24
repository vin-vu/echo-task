package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class DocumentCategorizer {

    @PostConstruct
    private void trainModel() {
        try {
            File trainingFile = new ClassPathResource("/data/training-data.txt").getFile();
            log.info("training file: {}", trainingFile);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
