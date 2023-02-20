package edu.school21.info.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(@NotNull Model model) {
        model.addAttribute("title", "Главная");
        return "home";
    }

    @GetMapping("/data")
    public String blogMain(@NotNull Model model) {
        model.addAttribute("title", "Данные");
        return "data-main";
    }

    @GetMapping("/operations")
    public String operationsMain(@NotNull Model model) {
        model.addAttribute("title", "Операции");
        return "operations-main";
    }
}
