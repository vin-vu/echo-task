package com.vince.echotask.nlp.depparser.strategy;

import java.util.Objects;

public class TraversalStrategyFactory {
    public static TraversalStrategy getStrategy(String tag) {
        if (Objects.equals(tag, "VB") || Objects.equals(tag, "VBP")) {
            return new VerbTraversalStrategy();
        } else if (Objects.equals(tag, "NN") || Objects.equals(tag, "NNP")) {
            return new NounTraversalStrategy();
        } else {
            throw new IllegalArgumentException("Unsupported POS tag: " + tag);
        }
    }
}
