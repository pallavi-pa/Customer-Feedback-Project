package com.example.demo.controller;

import com.example.demo.service.PraisingService;
import com.example.demo.model.Praising;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PraisingController {

    @Autowired
    private PraisingService praisingService;

    @GetMapping("/praising")
    public String showPraising(Model model) {
        List<Praising> praisingList = praisingService.getAllPraising();
        model.addAttribute("feedbacks", praisingList);
        return "praising";
    }
}
