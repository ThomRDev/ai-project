package com.thomrdev.aiproject.ai_project.infrastructure.in.http;

import com.thomrdev.aiproject.ai_project.domain.usecases.AnalyzeMessageUseCase;
import com.thomrdev.aiproject.ai_project.domain.usecases.ExtractTextFromVideoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class FileUploaderController {
    private final ExtractTextFromVideoUseCase extractTextFromVideoUseCase;
    private final AnalyzeMessageUseCase analyzeMessageUseCase;

    @GetMapping("/")
    public String viewHome() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadVideo(@RequestParam("videoFile") MultipartFile file, Model model) {

        try {
            String transcribedAudio = extractTextFromVideoUseCase.extractText(file.getBytes(), file.getOriginalFilename());
            String analyzedMessage = analyzeMessageUseCase.execute(transcribedAudio);

            model.addAttribute("errorMessage", null);
            model.addAttribute("transcript", transcribedAudio);

            return "response :: generatedResults";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error procesando el video: " + e.getMessage());
            return "index :: response";
        }
    }
}