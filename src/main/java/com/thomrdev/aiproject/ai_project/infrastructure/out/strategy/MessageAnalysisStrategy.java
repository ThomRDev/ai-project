package com.thomrdev.aiproject.ai_project.infrastructure.out.strategy;

public interface MessageAnalysisStrategy {
    boolean supports(String engine);
    String analyze(String messageJson);
}