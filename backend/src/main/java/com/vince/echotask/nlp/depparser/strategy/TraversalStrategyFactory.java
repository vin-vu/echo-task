package com.vince.echotask.nlp.depparser.strategy;

import com.vince.echotask.models.Intent;
import com.vince.echotask.nlp.depparser.strategy.completedtask.CompletedTaskVerbTraversalStrategy;

import java.util.Objects;

public class TraversalStrategyFactory {
    public static TraversalStrategy getStrategy(String tag, Intent intent) {
        if (intent == Intent.COMPLETED_TASK) {
            if (Objects.equals(tag, "VBN")) {
                return new CompletedTaskVerbTraversalStrategy();
            } else {
                throw new IllegalArgumentException("Unsupported POS tag: " + tag);
            }
        } else {
            if (Objects.equals(tag, "VB") || Objects.equals(tag, "VBP")) {
                return new VerbTraversalStrategy();
            } else if (Objects.equals(tag, "NN") || Objects.equals(tag, "NNP")) {
                return new NounTraversalStrategy();
            } else {
                throw new IllegalArgumentException("Unsupported POS tag: " + tag);
            }
        }
    }
}
