package com.vince.echotask.controller;

import com.vince.echotask.pojo.IntentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class EchoTaskController {

  @GetMapping("/detect-intent")
  String detectIntent(@RequestBody IntentRequest request) {
    log.info("Intent request: {}", request);
    return "Temp response containing intent";
  }
}
