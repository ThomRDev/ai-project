package com.thomrdev.aiproject.ai_project.domain.usecases;

public interface ExtractTextFromVideoUseCase {
    String extractText(byte[] videoBytes, String originalFilename);
}