package com.shehnaz.controller;

import com.shehnaz.model.Movie;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    private ChatClient chatClient;


    public MovieController(OpenAiChatModel chatModel){
        this.chatClient=ChatClient.create(chatModel);
    }

    @GetMapping("/api/movies")
    public List<String> getMovie(@RequestParam String name){

        List<String> movies=chatClient.prompt()
                .user(u->u.text("List of Top 5 movies {name}").param("name",name))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
        return movies;
    }

    @GetMapping("/api/movie")
    public Movie getMovieDetails(@RequestParam String name){
        BeanOutputConverter outputCon=new BeanOutputConverter<>(Movie.class);
        Movie movie=chatClient.prompt()
                .user(u->u.text("Give me Details of the Movie{name}"))
                .call()
                .entity(new BeanOutputConverter<Movie>(Movie.class));
        return movie;

    }

    @GetMapping("/api/moviesList")
    public List<Movie> getMovieDetailsList(@RequestParam String name){
        BeanOutputConverter outputCon=new BeanOutputConverter<>(Movie.class);
        List<Movie> movies=chatClient.prompt()
                .user(u->u.text("Give me Details of the Movie{name}"))
                .call()
                .entity(new BeanOutputConverter<List<Movie>>
                        (new ParameterizedTypeReference<List<Movie>>() {
                }));
        return movies;

    }

}
