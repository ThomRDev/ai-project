package com.thomrdev.aiproject.ai_project.application.services;

import com.thomrdev.aiproject.ai_project.domain.port.AudioToTextTranscriber;
import com.thomrdev.aiproject.ai_project.domain.port.VideoToAudioConverter;
import com.thomrdev.aiproject.ai_project.domain.usecases.ExtractTextFromVideoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ExtractTextFromVideoUseCaseImpl implements ExtractTextFromVideoUseCase {

    private final VideoToAudioConverter converter;
    private final AudioToTextTranscriber transcriber;

    @Override
    public String extractText(byte[] videoBytes, String originalFilename) {
        try {
            File tempVideoFile = File.createTempFile("video", originalFilename);
            try (FileOutputStream fos = new FileOutputStream(tempVideoFile)) {
                fos.write(videoBytes);
            }

            File audioFile = converter.convertToAudio(tempVideoFile);
            return transcriber.transcribe(audioFile);

        } catch (IOException e) {
            throw new RuntimeException("Error processing video", e);
        }
    }
}