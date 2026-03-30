package com.vince.echotask.nlp.task;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.util.Span;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class NERTestRunner implements CommandLineRunner {
    private final TaskFinderService taskFinderService;

    private final List<String> testInputs = List.of(
            "add buy groceries",
            "create remind me to call mom",
            "finished system design prep",
            "delete dentist appointment",
            "create vegas hotel for edc"
    );

    public NERTestRunner(TaskFinderService taskFinderService) {
        this.taskFinderService = taskFinderService;
    }

    @Override
    public void run(String... args) {
        for (String input : testInputs) {
            String[] tokens = input.split(" ");
            Span[] spans = taskFinderService.extractTask(tokens);

            log.info("INPUT: {}", input);

            if (spans.length == 0) {
                log.info("  → no entity found");
                continue;
            }

            for (Span s : spans) {
                String extracted = String.join(" ",
                        Arrays.copyOfRange(tokens, s.getStart(), s.getEnd()));

                log.info("  → span [{}..{}) type={} text='{}'",
                        s.getStart(),
                        s.getEnd(),
                        s.getType(),
                        extracted
                );
            }
        }
    }
}
