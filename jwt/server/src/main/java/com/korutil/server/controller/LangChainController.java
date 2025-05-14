package com.korutil.server.controller;

import com.korutil.server.service.llm.LangChainService;
import dev.langchain4j.data.message.AiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/langchain")
public class LangChainController {

    private final LangChainService langChainService;

    @PostMapping
    public ResponseEntity<String> chat(@RequestBody String message) {
        AiMessage aiMessage = langChainService.chat(message);

        return ResponseEntity.ok(aiMessage.text());
    }
}