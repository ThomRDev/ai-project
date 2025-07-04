package com.thomrdev.aiproject.ai_project.infrastructure.out.ffmpeg;

import com.thomrdev.aiproject.ai_project.domain.port.VideoToAudioConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FfmpegVideoToAudioConverter implements VideoToAudioConverter {

    @Override
    public File convertToAudio(File videoFile) {
        File audioFile = new File(videoFile.getAbsolutePath().replace(".mp4", ".wav"));

        // ffmpeg -i <ruta_video> -vn -acodec pcm_s16le -ar 16000 -ac 1 <ruta_audio.wav>
        ProcessBuilder pb = new ProcessBuilder("ffmpeg", "-i", videoFile.getAbsolutePath(), "-vn", "-acodec", "pcm_s16le", "-ar", "16000", "-ac", "1", audioFile.getAbsolutePath());
        try {
            Process process = pb.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error converting video to audio", e);
        }

        long audioFileSizeInBytes = audioFile.length();
        double audioFileSizeInMB = audioFileSizeInBytes / (1024.0 * 1024.0);

        if (audioFileSizeInMB > 10) {
            throw new IllegalArgumentException("El tamaño del audio supera el límite de 10MB");
        }
        System.out.printf("El tamaño del archivo de audio extraído es: %.2f MB%n", audioFileSizeInMB);

        return audioFile;
    }
}