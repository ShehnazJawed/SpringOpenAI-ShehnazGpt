package com.shehnaz.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AudioController {

    private OpenAiAudioTranscriptionModel audioModel;
    private OpenAiAudioSpeechModel speechModel;
    public AudioController(OpenAiAudioTranscriptionModel audioModel,OpenAiAudioSpeechModel speechModel){
        this.audioModel=audioModel;
        this.speechModel=speechModel;
    }

    @PostMapping("/api/speech-to-text")
    public String speechToText(@RequestParam MultipartFile file){
//        OpenAiAudioTranscriptionOptions.Builder options =
//                OpenAiAudioTranscriptionOptions.builder()
//                        .language("en")
//                        .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.SRT);
//        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(file.getResource(), options.build());
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(file.getResource());

        return audioModel.call(prompt)
                .getResult()
                .getOutput();
    }

    @PostMapping("/api/text-to-speech")
    public byte[] textToSpeech(@RequestParam String text){
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder().speed(1.5).voice(OpenAiAudioApi.SpeechRequest.Voice.FABLE)
                .build();
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text,options);
        return speechModel.call(prompt)
                .getResult()
                .getOutput();

    }

}
