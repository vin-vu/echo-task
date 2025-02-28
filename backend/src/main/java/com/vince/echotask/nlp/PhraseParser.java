package com.vince.echotask.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Component
public class PhraseParser {

    public void extractDescription() {

        String utterance = "Add item complete oil change on GR86 this weekend";

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos,depparse");
        props.setProperty("depparse.model", "edu/stanford/nlp/models/parser/nndep/english_UD.gz");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        CoreDocument document = pipeline.processToCoreDocument(utterance);
        pipeline.annotate(document);
        SemanticGraph dependencyParse = document.sentences().get(0).dependencyParse();
        log.info("document: {}", dependencyParse);

        IndexedWord root = dependencyParse.getFirstRoot();
        log.info("root: {}", root);

        ArrayList<IndexedWord> childrenListRoot = new ArrayList<>();
        for (IndexedWord child : dependencyParse.getChildList(root)) {
            childrenListRoot.add(child);
            log.info("child: {}, {}", child, child.value());
        }

        IndexedWord mainObject = null;
        for (IndexedWord child : childrenListRoot) {
            SemanticGraphEdge edge = dependencyParse.getEdge(root, child);
            log.info("child - edge: {}, {}, {}", child, edge, edge.getRelation().toString());
            if (Objects.equals(edge.getRelation().toString(), "obj")) {
                mainObject = child;
            }
        }
        log.info("main object: {}", mainObject);

        List<IndexedWord> mainObjectChildren = dependencyParse.getChildList(mainObject);
        List<IndexedWord> taskDescriptionWord = new ArrayList<>();
        boolean removedMetaWord = false;
        log.info("main object children: {}", mainObjectChildren);

        for (IndexedWord child : mainObjectChildren) {
            SemanticGraphEdge edge = dependencyParse.getEdge(mainObject, child);
            String relation = edge.getRelation().toString();
            log.info("edge to main object: {}, {}, {}", child, edge, relation);

            // remove meta word - task, reminder, todo, etc.
            if (!removedMetaWord && Objects.equals(relation, "compound")) {
                removedMetaWord = true;
                continue;
            }
            taskDescriptionWord.add(child);
        }
        log.info("task description words: {}", taskDescriptionWord);
    }
}
