package com.vince.echotask.nlp.intentcategorizer;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

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
@Service
public class DoccatModelService {

    private final Tokenizer tokenizer;
    private final DocumentCategorizerME intentCategorizer;

    public DoccatModelService(Tokenizer tokenizer,
                              @Value("${app.trainModelOnStartup:false}") boolean trainModelOnStartup) throws Exception {
        this.tokenizer = tokenizer;

        if (trainModelOnStartup) {
            trainModel();
        }

        this.intentCategorizer = loadModel();
    }

    private DocumentCategorizerME loadModel() throws Exception {
        ClassPathResource modelResource = new ClassPathResource("nlp/en-doccat-v4.bin");

        try (InputStream modelInput = modelResource.getInputStream()) {
            DoccatModel model = new DoccatModel(modelInput);
            return new DocumentCategorizerME(model);
        }
    }

    private void trainModel() throws IOException {
        InputStreamFactory inputStreamFactory = loadAndTokenizeTrainingData();

        try (ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
             ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream)) {

            TrainingParameters params = TrainingParameters.defaultParams();
            params.put(TrainingParameters.ITERATIONS_PARAM, "150");
            params.put(TrainingParameters.CUTOFF_PARAM, "5");

            DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());
            saveModel(model);
        }

        log.info("Model training complete");
    }

    private void saveModel(DoccatModel model) throws IOException {
        Path modelPath = Paths.get("backend/src/main/resources/nlp/en-doccat-v4.bin");

        try (OutputStream modelOut = new BufferedOutputStream(Files.newOutputStream(modelPath))) {
            model.serialize(modelOut);
        }

        log.info("Model saved at: {}", modelPath);
    }

    private InputStreamFactory loadAndTokenizeTrainingData() throws IOException {
        File trainingFile = new ClassPathResource("data/doccat-training-v4.txt").getFile();
        List<String> processedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(trainingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                if (parts.length < 2) {
                    log.warn("Skipping malformed line: {}", line);
                    continue;
                }

                String intent = parts[0];
                String[] tokens = tokenizer.getTranscriptTokens(parts[1]);

                processedLines.add(intent + " " + String.join(" ", tokens));
            }
        }

        Path tempFile = Paths.get("backend/src/main/resources/data/doccat-training-processed.txt");
        Files.write(tempFile, processedLines, StandardCharsets.UTF_8);

        log.info("Processed {} training samples", processedLines.size());

        return new MarkableFileInputStreamFactory(tempFile.toFile());
    }

    public SortedMap<Double, Set<String>> categorizeIntent(String[] phraseTokens) {
        return intentCategorizer.sortedScoreMap(phraseTokens);
    }
}
