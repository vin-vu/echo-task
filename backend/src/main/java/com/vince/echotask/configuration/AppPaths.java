package com.vince.echotask.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppPaths {
    private String dataDir;
    private String modelPath;
    private String trainingProcessedPath;
    private String trainingRawPath;

    private String taskFinderTrainingRawPath;
    private String taskFinderModelPath;
}

