package com.vince.echotask.nlp.intentcategorizer;

import com.vince.echotask.models.Intent;
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

    // must run below method to train a new Intent Categorizer  model
    public void trainModel() throws IOException {
        DoccatModel doccatModel;

        // load processed training file
        InputStreamFactory inputStreamFactory = loadAndTokenizeTrainingData();

        // reading training file and train model
        try (ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, "UTF-8");
             ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream)) {
            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, "150");
            params.put(TrainingParameters.CUTOFF_PARAM, "5");
            doccatModel = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());
        }

        Path modelPath = Paths.get("backend/src/main/resources/nlp/en-doccat-v3.bin");

        try (OutputStream modelOut = new BufferedOutputStream(Files.newOutputStream(modelPath))) {
            doccatModel.serialize(modelOut);
        }
        log.info("model training complete - saved as en-doccat-v3.bin");
    }

    private InputStreamFactory loadAndTokenizeTrainingData() throws IOException {

        // load training file
        File trainingFile = new ClassPathResource("/data/doccat-training.txt").getFile();
        List<String> processedLines = new ArrayList<>();

        // normalize and tokenize training file
        try (BufferedReader reader = new BufferedReader(new FileReader(trainingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                if (parts.length < 2) {
                    log.warn("Skipping malformed line in training data: {}", line);
                    continue;
                }
                String intent = parts[0];
                String taskDescription = parts[1];

                String[] tokens = tokenizer.getTranscriptTokens(taskDescription);
                String processedTokens = String.join(" ", tokens);
                processedLines.add(intent + " " + processedTokens);
            }
        }

        Path tempTrainingFile = Paths.get("backend/src/main/resources/data/doccat-training-processed.txt");
        Files.write(tempTrainingFile, processedLines, StandardCharsets.UTF_8);

        log.info("Total training samples after preprocessing: {}", processedLines.size());
        log.info("Processed training data saved at: {}", tempTrainingFile);

        return new MarkableFileInputStreamFactory(tempTrainingFile.toFile());
    }

    public SortedMap<Double, Set<String>> categorizeIntent(String[] phraseTokens) throws IOException {
        ClassPathResource modelResource = new ClassPathResource("nlp/en-doccat-v3.bin");

        try (InputStream modelInput = modelResource.getInputStream()) {
            DoccatModel doccatModel = new DoccatModel(modelInput);
            DocumentCategorizerME intentCategorizer = new DocumentCategorizerME(doccatModel);
            return intentCategorizer.sortedScoreMap(phraseTokens);
        }
    }

    public Intent getBestIntent(SortedMap<Double, Set<String>> rankedIntentScores) {
        // currently not error handling if multiple intents share single probability
        // meaning more than 1 intent can be shown in a set ex. add 33% delete 33% completed 33%
        Set<String> bestIntents = rankedIntentScores.get(rankedIntentScores.lastKey());
        return Intent.valueOf(bestIntents.iterator().next());
    }
}
