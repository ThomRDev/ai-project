package com.thomrdev.aiproject.ai_project.infrastructure.out.vosk;

import com.thomrdev.aiproject.ai_project.domain.port.AudioToTextTranscriber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class VoskTranscriber implements AudioToTextTranscriber {

    private Model model;
    private final String modelPath;

    @PostConstruct
    public void init() throws IOException {

        LibVosk.setLogLevel(LogLevel.WARNINGS);

        if (modelPath == null || modelPath.isBlank()) throw new IllegalStateException("VOSK_MODEL_PATH is not set");
        model = new Model(modelPath);
    }

    @Override
    public String transcribe(File audioFile) {
        try (InputStream ais = new FileInputStream(audioFile);
             Recognizer recognizer = new Recognizer(model, 16000)) {

            byte[] buffer = new byte[4096];
            while (true) {
                int nbytes = ais.read(buffer);
                if (nbytes < 0) break;
                recognizer.acceptWaveForm(buffer, nbytes);
            }
            return recognizer.getFinalResult();
        } catch (IOException e) {
            throw new RuntimeException("Error transcribing audio", e);
        }
    }
}