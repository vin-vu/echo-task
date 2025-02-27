package com.vince.echotask.nlp;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Component
public class PhraseParser {

    private static final ParserModel parseModel;
    private static final Parser parser;

    static {
        try (InputStream parserStream = new ClassPathResource("nlp/opennlp-en-parser-chunking.bin").getInputStream()) {
            parseModel = new ParserModel(parserStream);
            parser = ParserFactory.create(parseModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
