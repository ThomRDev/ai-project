package com.thomrdev.aiproject.ai_project.infrastructure.out.strategy;

import com.thomrdev.aiproject.ai_project.infrastructure.out.vosk.VoskTranscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@RequiredArgsConstructor
@Component
public class VoskStrategy implements TranscriberStrategy {

    private final VoskTranscriber transcriber;

    @Override
    public boolean supports(String type) {
        return "vosk".equalsIgnoreCase(type);
    }

    @Override
    public String transcribe(File audio) {
        return transcriber.transcribe(audio);
    }
}