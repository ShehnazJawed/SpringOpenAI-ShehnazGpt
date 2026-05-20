package com.shehnaz.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.ai.embedding.EmbeddingModel;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ShehnazController {

    private OpenAiChatModel chatModel;

    private ChatClient chatClient;
    @Qualifier("openAiEmbeddingModel")
    private EmbeddingModel embeddingModel;

    // 01
//    public ShehnazController(OpenAiChatModel chatModel){
//        this.chatModel=chatModel;
//
//    }


//    @GetMapping("api/answer/{message}")
//    public String getAnswer(@PathVariable String message){
//        String response = chatModel.call(message);
//        return response;
//    }

    // 02
//public ShehnazController(OpenAiChatModel chatModel){
//    this.chatClient=ChatClient.create(chatModel);
//}


//    @GetMapping("api/answer/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//        String response = chatClient.prompt(message).call().content();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }


    // 03
    //    public ShehnazController(OpenAiChatModel chatModel){
//        this.chatClient=ChatClient.create(chatModel);
//    }


//    @GetMapping("api/answer/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//        ChatResponse chatResponse = chatClient.prompt(message).call().chatResponse();
//        System.out.println(chatResponse.getMetadata().getModel());
//        String response = chatResponse.getResult().getOutput().getText();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }



    // 04
    // chat memory advisor for context window means for remember chat
    //To make our Application Statefull
//    ChatMemory chatMemory= MessageWindowChatMemory.builder().build();
//
//    public ShehnazController(ChatClient.Builder builder){
//        this.chatClient=builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();
//
//    }
//
//    @GetMapping("api/answer/{message}")
//    public ResponseEntity<String> getAnswer(@PathVariable String message){
//        ChatResponse chatResponse = chatClient.prompt(message).user(message).advisors(a->a.param(ChatMemory.CONVERSATION_ID,"shehnaz-chat")).call().chatResponse();
//        System.out.println(chatResponse.getMetadata().getModel());
//        String response = chatResponse.getResult().getOutput().getText();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

//05
        public ShehnazController(OpenAiChatModel chatModel){
        this.chatClient=ChatClient.create(chatModel);
    }
    @GetMapping("api/answer/{message}")
    public ResponseEntity<String> getAnswer(@PathVariable String message){
        ChatResponse chatResponse = chatClient.prompt(message).user(message).advisors(a->a.param(ChatMemory.CONVERSATION_ID,"shehnaz-chat")).call().chatResponse();
        System.out.println(chatResponse.getMetadata().getModel());
        String response = chatResponse.getResult().getOutput().getText();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String type,@RequestParam String year,@RequestParam String lang){
            String temp="""
                I want to watch a {type} movie tonight with good rating,\s
                               looking  for movies around this year {year}.\s
                               The  language im looking for is {lang}.
                               Suggest one specific movie and tell me the cast and length of the movie.
                
                
                               response format should be:
                               1. Movie Name
                               2. basic plot
                               3. cast
                               4. length
                               5. IMDB rating
                """;
        PromptTemplate promptTemplate = new PromptTemplate(temp);
        Prompt prompt=promptTemplate.create(Map.of(
                "type", type,
                "year", year,
                "lang", lang
        ));
        String response = chatClient.prompt(prompt)
                .call()
                .content();
        return response;
    }

    @PostMapping("/api/embeddings")
    public float[] embeddings(@RequestParam String text)
    {
        return embeddingModel.embed(text);
    }

}
