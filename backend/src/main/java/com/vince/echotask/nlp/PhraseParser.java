package com.vince.echotask.nlp;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

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

}
