package com.eazybytes.springai.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.ai.audio.tts.TextToSpeechOptions;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class AudioController {

    private final OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private final TextToSpeechModel speechModel;

    public AudioController(OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel, TextToSpeechModel textToSpeechModel) {
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.speechModel = textToSpeechModel;
    }

    @GetMapping("/transcribe")
    String transcribe(@Value("classpath:SpringAI.mp3") Resource audioFile) {
        return openAiAudioTranscriptionModel.call(audioFile);
    }

    @GetMapping("/transcribe-options")
    String transcribeWithOptions(@Value("classpath:SpringAI.mp3") Resource audioFile) {
        AudioTranscriptionResponse audioTranscriptionResponse = openAiAudioTranscriptionModel.call(
                new AudioTranscriptionPrompt(audioFile, OpenAiAudioTranscriptionOptions.builder()
                        .prompt("Talking about Spring AI")
                        .language("en")
                        .temperature(0.5f)
                        .responseFormat(OpenAiAudioApi.TranscriptResponseFormat.VTT)
                        .build()
                ));
        return audioTranscriptionResponse.getResult().getOutput();
    }

    @GetMapping("/speech")
    String speech(String message) throws IOException {
        byte[] audioBytes =  speechModel.call(message);
        Path path = Paths.get("output.mp3");
        Files.write(path, audioBytes);
        return "MP3 saved successfully to " + path.toAbsolutePath();
    }

    @GetMapping("/speech-options")
    String speechWithOptions(String message) throws IOException {
        TextToSpeechResponse textToSpeechOptions = speechModel.call(
                new TextToSpeechPrompt(message, TextToSpeechOptions.builder()
                        .voice(OpenAiAudioApi.SpeechRequest.Voice.ECHO.getValue())
                        .speed(4.0)
                        .format(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3.getValue())
                        .build())
        );
        Path path = Paths.get("speech-options-output.mp3");
        Files.write(path, textToSpeechOptions.getResult().getOutput());
        return "MP3 saved successfully to " + path.toAbsolutePath();
    }
}
