package com.thomrdev.aiproject.ai_project.infrastructure.out.strategy;

import java.io.File;

public interface TranscriberStrategy {
    boolean supports(String type);
    String transcribe(File audio);
}