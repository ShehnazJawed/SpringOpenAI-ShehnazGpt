package com.shehnaz.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
public class ImageController {

    private ChatClient chatClient;

    private OpenAiImageModel imageModel;

    public ImageController(ChatClient.Builder builder,OpenAiImageModel imageModel){
        this.chatClient=builder.build();
        this.imageModel=imageModel;
    }

//    @GetMapping("api/image/{prompt}")
//    public  String generateImage(@PathVariable String prompt) throws IOException {
//        ImagePrompt promptImage = new ImagePrompt(prompt);
//        ImageResponse response = imageModel.call(promptImage);
////        return response.getResult().getOutput().getUrl();
//       String json= response.getResult().getOutput().getB64Json();
//        byte[] imageBytes = Base64.getDecoder().decode(json);
//        Files.write(Paths.get("generated_image.png"),imageBytes);
//        return "Image is generated";
//
//
//    }
@GetMapping("api/image/{prompt}")
public String generateImage(@PathVariable String prompt) throws IOException {
    ImagePrompt promptImage = new ImagePrompt(prompt, OpenAiImageOptions
            .builder()
            .quality("high")
            .height(1024)
            .width(1024)
            .build());
    ImageResponse response = imageModel.call(promptImage);
    // String url= response.getResult().getOutput().getUrl();
    //System.out.println(url);
    String json= response.getResult().getOutput().getB64Json();
    byte[] imageBytes = Base64.getDecoder().decode(json);
    Files.write(Paths.get("generated_image.png"), imageBytes);
    return "Image is generated";

}
    @PostMapping("/api/describe")
    public String describeImage(@RequestParam String query,
                                @RequestParam MultipartFile file)
    {
        return chatClient .prompt().user(us-> us.text(query)
                        .media(MimeTypeUtils.IMAGE_JPEG, file.getResource()))
                //                   .media(MimeTypeUtils.IMAGE_PNG, file.getResource()))

                .call()
                .content();
    }

}
