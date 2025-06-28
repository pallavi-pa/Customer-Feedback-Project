package com.example.demo.controller;

import com.example.demo.service.ComplaintService;
import com.example.demo.model.Complaint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @GetMapping("/complaints")
    public String showComplaints(Model model) {
        List<Complaint> complaints = complaintService.getAllComplaints();
        model.addAttribute("feedbacks", complaints);
        return "complaints";
    }
}
