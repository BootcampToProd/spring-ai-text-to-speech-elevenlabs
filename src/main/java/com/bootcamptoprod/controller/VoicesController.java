package com.bootcamptoprod.controller;

import org.springframework.ai.elevenlabs.api.ElevenLabsVoicesApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voices")
public class VoicesController {

    private final ElevenLabsVoicesApi voicesApi;

    public VoicesController(@Value("${spring.ai.elevenlabs.api-key}") String apiKey) {
        this.voicesApi = ElevenLabsVoicesApi.builder()
                .apiKey(apiKey)
                .build();
    }

    @GetMapping
    public ResponseEntity<ElevenLabsVoicesApi.Voices> getAllVoices() {
        return voicesApi.getVoices();
    }

    @GetMapping("/settings/default")
    public ResponseEntity<ElevenLabsVoicesApi.VoiceSettings> getDefaultVoiceSettings() {
        return voicesApi.getDefaultVoiceSettings();
    }

    @GetMapping("/{voiceId}")
    public ResponseEntity<ElevenLabsVoicesApi.Voice> getVoiceDetails(@PathVariable String voiceId) {
        return voicesApi.getVoice(voiceId);
    }

    @GetMapping("/{voiceId}/settings")
    public ResponseEntity<ElevenLabsVoicesApi.VoiceSettings> getVoiceSettings(@PathVariable String voiceId) {
        return voicesApi.getVoiceSettings(voiceId);
    }
}
