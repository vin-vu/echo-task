package com.vince.echotask.nlp.depparser;

import com.vince.echotask.models.Intent;
import com.vince.echotask.nlp.depparser.strategy.TraversalStrategy;
import com.vince.echotask.nlp.depparser.strategy.TraversalStrategyFactory;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DependencyParser {

    private static final StanfordCoreNLP pipeline;

    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos,depparse");
        props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/english_UD.gz");
        pipeline = new StanfordCoreNLP(props);
    }

    public SemanticGraph createDependencyParseTree(String phrase) {
        CoreDocument document = pipeline.processToCoreDocument(phrase);
        pipeline.annotate(document);
        SemanticGraph dependencyParse = document.sentences().get(0).dependencyParse();
        log.info("document: {}", dependencyParse);
        return dependencyParse;
    }

    public String extractDescription(SemanticGraph dependencyParse, Intent intent) {

        IndexedWord root = dependencyParse.getFirstRoot();
        String rootPOS = root.tag();
        log.info("root - POS: {} - {}", root, rootPOS);

        TraversalStrategy strategy = TraversalStrategyFactory.getStrategy(rootPOS, intent);
        List<IndexedWord> taskDescriptionWords = strategy.traverse(dependencyParse, root, root);
        taskDescriptionWords.sort(Comparator.comparingInt(IndexedWord::index));

        String taskDescription = taskDescriptionWords.stream().map(IndexedWord::word).collect(Collectors.joining(" "));
        log.info("task description words: {}", taskDescription);
        return taskDescription;
    }

}
