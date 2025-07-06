package com.thomrdev.aiproject.ai_project.infrastructure.config;

import com.thomrdev.aiproject.ai_project.domain.port.AudioToTextTranscriber;
import com.thomrdev.aiproject.ai_project.domain.port.MessageAnalyzer;
import com.thomrdev.aiproject.ai_project.infrastructure.out.strategy.MessageAnalysisStrategy;
import com.thomrdev.aiproject.ai_project.infrastructure.out.strategy.TranscriberStrategy;
import com.thomrdev.aiproject.ai_project.infrastructure.out.vosk.VoskTranscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;
import java.util.List;

@Configuration
public class BeanConfiguration {

    @Bean
    public VoskTranscriber voskTranscriber(@Value("${vosk.model.path}") String modelPath) {
        return new VoskTranscriber(modelPath);
    }

    @Primary
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

    @Bean
    public MessageAnalyzer messageAnalyzer(
            List<MessageAnalysisStrategy> strategies,
            @Value("${ai.engine}") String engine
    ) {
        return messageJson -> strategies.stream()
                .filter(s -> s.supports(engine))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy for engine: " + engine))
                .analyze(messageJson);
    }
}

/*
ExtractTextFromVideoUseCaseImpl
→ llama a AudioToTextTranscriber.transcribe(...)

AudioToTextTranscriber (definido en BeanConfiguration)
→ selecciona la estrategia correcta según transcriber.engine (por ejemplo, "vosk")

VoskStrategy (es un @Component y está en la lista de estrategias)
→ llama a VoskTranscriber.transcribe(...)

VoskTranscriber
→ es un @Bean definido en BeanConfiguration, no lleva @Component
→ su constructor recibe vosk.model.path desde el application.properties
* */