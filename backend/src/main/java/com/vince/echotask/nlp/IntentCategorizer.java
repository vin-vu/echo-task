package com.vince.echotask.nlp;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class IntentCategorizer {

    @PostConstruct
    private void trainModel() {
        DoccatModel doccatModel;
        try {

            // load training file
            File trainingFile = new ClassPathResource("/data/training-data.txt").getFile();
            InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(trainingFile);

            // reading training file and train model
            try (ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
                 ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream)) {
                TrainingParameters params = new TrainingParameters();
                params.put(TrainingParameters.ITERATIONS_PARAM, "100");
                params.put(TrainingParameters.CUTOFF_PARAM, "0");
                doccatModel = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());
            }

            Path modelPath = Paths.get("backend/src/main/resources/nlp/en-doccat.bin");

            //  try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelPath))) {
            try (OutputStream modelOut = new BufferedOutputStream(Files.newOutputStream(modelPath))) {
                doccatModel.serialize(modelOut);
            }

            log.info("model training complete - saved as en-doccat.bin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
