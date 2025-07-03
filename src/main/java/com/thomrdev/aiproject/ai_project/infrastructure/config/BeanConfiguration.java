package com.thomrdev.aiproject.ai_project.infrastructure.config;

import com.thomrdev.aiproject.ai_project.domain.port.AudioToTextTranscriber;
import com.thomrdev.aiproject.ai_project.infrastructure.out.strategy.TranscriberStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    public AudioToTextTranscriber audioToTextTranscriber(
            List<TranscriberStrategy> strategies,
            @Value("${transcriber.engine}") String engineTranscriber
    ) {
//        return new AudioToTextTranscriber() {
//            @Override
//            public String transcribe(File audioFile) {
//                return strategies.stream()
//                        .filter(s -> s.supports("vosk"))
//                        .findFirst()
//                        .orElseThrow()
//                        .transcribe(audioFile);
//            }
//        };
        return audioFile -> strategies.stream()
                .filter(s -> s.supports(engineTranscriber))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No strategy for: " + engineTranscriber))
                .transcribe(audioFile);
    }
}