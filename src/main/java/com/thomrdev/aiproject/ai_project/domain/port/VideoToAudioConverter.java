package com.thomrdev.aiproject.ai_project.domain.port;
import java.io.File;

public interface VideoToAudioConverter {
    File convertToAudio(File videoFile);
}