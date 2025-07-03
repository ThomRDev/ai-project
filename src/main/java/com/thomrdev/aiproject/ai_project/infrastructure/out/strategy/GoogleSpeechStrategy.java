package com.thomrdev.aiproject.ai_project.infrastructure.out.strategy;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class GoogleSpeechStrategy implements TranscriberStrategy {

    @Override
    public boolean supports(String type) {
        return "google".equalsIgnoreCase(type);
    }

    @Override
    public String transcribe(File audio) {
        throw new UnsupportedOperationException("GoogleSpeech not implemented yet");
    }
}