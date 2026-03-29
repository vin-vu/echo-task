package com.vince.echotask.nlp.ner;

import com.vince.echotask.configuration.AppPaths;
import opennlp.tools.namefind.*;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@Service
public class TaskFinderService {

    private final AppPaths appPaths;
    private final NameFinderME taskFinder;

    public TaskFinderService(AppPaths appPaths,
                             @Value("${app.trainTaskFinderModel:false}") boolean trainTaskFinderModel) throws Exception {
        this.appPaths = appPaths;

        if (trainTaskFinderModel) {
            trainTaskFinder();
        }

        this.taskFinder = loadTaskFinder();
    }

    private NameFinderME loadTaskFinder() throws Exception {
        Path modelPath = Paths.get(appPaths.getTaskFinderModelPath());

        if (Files.exists(modelPath)) {
            try (InputStream modelInput = Files.newInputStream(modelPath)) {
                return new NameFinderME(new TokenNameFinderModel(modelInput));
            }
        }

        ClassPathResource modelResource = new ClassPathResource("nlp/task-finder/en-task-ner.bin");
        try (InputStream modelInput = modelResource.getInputStream()) {
            return new NameFinderME(new TokenNameFinderModel(modelInput));
        }
    }

    public void trainTaskFinder() throws IOException {
        TokenNameFinderFactory factory = TokenNameFinderFactory.create(null, null, Collections.emptyMap(),
                new BioCodec());
        File trainingFile = new File(appPaths.getTaskFinderTrainingRawPath());

        ObjectStream<String> lineStream =
                new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile), StandardCharsets.UTF_8);

        TokenNameFinderModel trainedModel;
        try (ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream)) {
            trainedModel = NameFinderME.train("eng", "task", sampleStream, TrainingParameters.defaultParams(),
                    factory);
        }

        File modelFile = new File(appPaths.getTaskFinderModelPath());
        try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile))) {
            trainedModel.serialize(modelOut);

        }
    }

}
