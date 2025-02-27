package com.vince.echotask.nlp;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@Component
public class PhraseParser {

    public void extractDescription() {

        String utterance = "Add new task complete oil change on GR86 this weekend";
        String utterance2 = "Add new task doctor appointment is at 8am tomorrow";

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

        ArrayList<IndexedWord> childrenList = new ArrayList<>();
        for (IndexedWord child : dependencyParse.getChildList(root)) {
            childrenList.add(child);
            log.info("child: {}, {}", child, child.value());
        }

        IndexedWord mainObject = null;
        for (IndexedWord child : childrenList) {
            SemanticGraphEdge edge = dependencyParse.getEdge(root, child);
            log.info("child - edge: {}, {}, {}", child, edge, edge.getRelation().toString());
            if (Objects.equals(edge.getRelation().toString(), "obj")) {
                mainObject = child;
            }
        }
        log.info("main object: {}", mainObject);


//        CoreDocument document2 = pipeline.processToCoreDocument(utterance2);
//        pipeline.annotate(document2);
//        SemanticGraph dependencyParse2 = document2.sentences().get(0).dependencyParse();
//        log.info("document: {}", dependencyParse2);
//
//        CoreDocument document3 = pipeline.processToCoreDocument("Add new task class is at 8am tomorrow.");
//        pipeline.annotate(document3);
//        SemanticGraph dependencyParse3 = document3.sentences().get(0).dependencyParse();
//        log.info("document: {}", dependencyParse3);

    }

}
