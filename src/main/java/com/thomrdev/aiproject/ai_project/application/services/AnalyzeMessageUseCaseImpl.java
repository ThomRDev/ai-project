package com.thomrdev.aiproject.ai_project.application.services;

import com.thomrdev.aiproject.ai_project.domain.port.MessageAnalyzer;
import com.thomrdev.aiproject.ai_project.domain.usecases.AnalyzeMessageUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyzeMessageUseCaseImpl implements AnalyzeMessageUseCase {

    private final MessageAnalyzer analyzer;

    @Override
    public String execute(String messageJson) {
        return analyzer.analyze(messageJson);
    }
}
