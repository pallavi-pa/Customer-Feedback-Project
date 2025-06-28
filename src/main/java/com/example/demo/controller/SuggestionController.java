package com.example.demo.controller;

import com.example.demo.service.SuggestionService;
import com.example.demo.model.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SuggestionController {

    @Autowired
    private SuggestionService suggestionService;

    @GetMapping("/suggestions")
    public String showSuggestions(Model model) {
        List<Suggestion> suggestions = suggestionService.getAllSuggestions();
        model.addAttribute("feedbacks", suggestions);
        return "suggestions";
    }
}
