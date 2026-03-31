package com.vince.echotask.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app")
public class AppPaths {
    private String dataDir;
    private Doccat doccat = new Doccat();
    private TaskFinder taskfinder = new TaskFinder();

    @Data
    public static class Doccat {
        private String modelPath;
        private String trainingProcessedPath;
        private String trainingRawPath;
    }

    @Data
    public static class TaskFinder {
        private String modelPath;
        private String trainingRawPath;
    }
}

