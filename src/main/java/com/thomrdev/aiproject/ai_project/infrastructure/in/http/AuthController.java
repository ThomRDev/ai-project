package com.thomrdev.aiproject.ai_project.infrastructure.in.http;

import com.thomrdev.aiproject.ai_project.infrastructure.persistence.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginView(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
}
