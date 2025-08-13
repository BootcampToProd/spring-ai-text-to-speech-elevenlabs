package com.bootcamptoprod.controller;

import com.bootcamptoprod.dto.TextToSpeechRequest;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.elevenlabs.ElevenLabsTextToSpeechModel;
import org.springframework.ai.elevenlabs.ElevenLabsTextToSpeechOptions;
import org.springframework.ai.elevenlabs.api.ElevenLabsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tts")
public class TextToSpeechController {

    private final ElevenLabsTextToSpeechModel textToSpeechModel;

    @Autowired
    public TextToSpeechController(ElevenLabsTextToSpeechModel textToSpeechModel) {
        this.textToSpeechModel = textToSpeechModel;
    }

    @PostMapping(value = "/speak", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> speak(@RequestBody TextToSpeechRequest request) {
        var voiceSettings = new ElevenLabsApi.SpeechRequest.VoiceSettings(
                request.stability(), request.similarityBoost(), request.style(), request.useSpeakerBoost(), request.speed()
        );

        var textToSpeechOptions = ElevenLabsTextToSpeechOptions.builder()
                .model("eleven_multilingual_v2")
                .voiceId(request.voiceId())
                .voiceSettings(voiceSettings)
                .outputFormat(ElevenLabsApi.OutputFormat.MP3_44100_128.getValue())
                .build();

        textToSpeechOptions.setEnableLogging(Boolean.TRUE);

        var textToSpeechPrompt = new TextToSpeechPrompt(request.text(), textToSpeechOptions);
        var textToSpeechResponse = textToSpeechModel.call(textToSpeechPrompt);

        byte[] audio = textToSpeechResponse.getResult().getOutput();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("audio/mpeg"));
        headers.setContentLength(audio.length);

        return new ResponseEntity<>(audio, headers, HttpStatus.OK);
    }
}
