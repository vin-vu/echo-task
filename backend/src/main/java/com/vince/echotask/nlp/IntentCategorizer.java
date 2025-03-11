package com.vince.echotask.nlp;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

@Slf4j
@Component
public class IntentCategorizer {

    @Autowired
    Tokenizer tokenizer;

    public void trainModel() {
        DoccatModel doccatModel;
        try {
            // load training file
            File trainingFile = new ClassPathResource("/data/doccat-training.txt").getFile();
            List<String> processedLines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(trainingFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ", 2);
                    String intent = parts[0];
                    String taskDescription = parts[1];

                    String[] tokens = tokenizer.getTranscriptTokens(taskDescription);
                    String processedTokens = String.join(" ", tokens);

                    processedLines.add(intent + " " + processedTokens);
                }
            }

            Path tempTrainingFile = Paths.get("backend/src/main/resources/data/doccat-training-processed.txt");
            Files.write(tempTrainingFile, processedLines, StandardCharsets.UTF_8);

            InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(tempTrainingFile.toFile());

            // reading training file and train model
            try (ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
                 ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream)) {
                TrainingParameters params = new TrainingParameters();
                params.put(TrainingParameters.ITERATIONS_PARAM, "100");
                params.put(TrainingParameters.CUTOFF_PARAM, "0");
                doccatModel = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());
            }

            Path modelPath = Paths.get("backend/src/main/resources/nlp/en-doccat-v2.bin");

            //  try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelPath))) {
            try (OutputStream modelOut = new BufferedOutputStream(Files.newOutputStream(modelPath))) {
                doccatModel.serialize(modelOut);
            }

            log.info("model training complete - saved as en-doccat-v2.bin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SortedMap<Double, Set<String>> categorizeIntent(String[] phraseTokens) throws IOException {
        ClassPathResource modelResource = new ClassPathResource("nlp/en-doccat-v2.bin");

        try (InputStream modelInput = modelResource.getInputStream()) {
            DoccatModel doccatModel = new DoccatModel(modelInput);
            DocumentCategorizerME intentCategorizer = new DocumentCategorizerME(doccatModel);
            return intentCategorizer.sortedScoreMap(phraseTokens);
        }
    }
}
