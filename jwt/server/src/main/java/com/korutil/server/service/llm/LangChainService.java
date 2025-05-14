package com.korutil.server.service.llm;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LangChainService {

    private final ChatLanguageModel chatLanguageModel;

    public AiMessage chat(String userMessage) {
        ChatMessage message = new UserMessage(userMessage);
        return chatLanguageModel.generate(message).content();
    }
}
