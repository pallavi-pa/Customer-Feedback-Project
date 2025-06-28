package com.example.demo.controller;

import com.example.demo.model.Feedback;
import com.example.demo.model.User;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class ViewController {

    private static final String SESSION_USERNAME = "username";
    private static final String SESSION_ROLE = "role";

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    // Home, Signup, Login
    @GetMapping({"/", "/home"})
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(required = false) String role,
                               Model model) {
        if (role == null || role.trim().isEmpty()) {
            model.addAttribute("error", "Role is required.");
            return "signup";
        }

        Optional<User> existingUser = userRepository.findByUsername(username.trim());
        if (existingUser.isPresent()) {
            model.addAttribute("error", "Username already exists.");
            return "signup";
        }

        User newUser = new User(username.trim(), password.trim(), role.toUpperCase().trim());
        userRepository.save(newUser);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                              @RequestParam String password,
                              @RequestParam(required = false) String role,
                              HttpSession session,
                              Model model) {
        if (role == null || role.trim().isEmpty()) {
            model.addAttribute("error", "Role is required.");
            return "login";
        }

        Optional<User> optionalUser = userRepository.findByUsername(username.trim());
        if (optionalUser.isEmpty()) {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(password.trim()) ||
                !user.getRole().equalsIgnoreCase(role.trim())) {
            model.addAttribute("error", "Invalid credentials or role.");
            return "login";
        }

        session.setAttribute(SESSION_USERNAME, username.trim());
        session.setAttribute(SESSION_ROLE, role.toUpperCase().trim());

        return "ADMIN".equalsIgnoreCase(role.trim()) ?
                "redirect:/admin/dashboard" : "redirect:/user/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private boolean isUserLoggedIn(HttpSession session, String expectedRole) {
        String username = (String) session.getAttribute(SESSION_USERNAME);
        String role = (String) session.getAttribute(SESSION_ROLE);
        return username != null && expectedRole.equalsIgnoreCase(role);
    }

    // ---------------- USER SECTION ----------------

    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";
        return "userdashboard";
    }

    @GetMapping("/user/feedback")
    public String feedbackForm(HttpSession session) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";
        return "feedback";
    }

    @PostMapping("/user/feedback")
    public String submitFeedback(@RequestParam String message,
                                 @RequestParam String feedbackType,
                                 @RequestParam Integer rating,
                                 HttpSession session,
                                 Model model) {
        String username = (String) session.getAttribute(SESSION_USERNAME);
        if (username == null || username.isEmpty()) return "redirect:/login";

        if (message.trim().isEmpty() || feedbackType.trim().isEmpty() || rating < 1 || rating > 5) {
            model.addAttribute("error", "Please fill all feedback fields correctly.");
            return "feedback";
        }

        Feedback feedback = new Feedback();
        feedback.setUsername(username);
        feedback.setMessage(message.trim());
        feedback.setFeedbackType(feedbackType.trim());
        feedback.setRating(rating);
        feedback.setCreatedAt(LocalDateTime.now());

        feedbackRepository.save(feedback);
        return "redirect:/user/dashboard";
    }

    @GetMapping("/user/viewmyfeedback")
    public String viewMyFeedback(HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";

        String username = (String) session.getAttribute(SESSION_USERNAME);
        List<Feedback> feedbackList = feedbackRepository.findByUsername(username);
        model.addAttribute("feedbackList", feedbackList);
        return "viewmyfeedback";
    }

    @PostMapping("/user/feedback/delete")
    public String deleteFeedback(@RequestParam Long id, HttpSession session) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";

        Optional<Feedback> feedback = feedbackRepository.findById(id);
        if (feedback.isPresent() &&
                feedback.get().getUsername().equals(session.getAttribute(SESSION_USERNAME))) {
            feedbackRepository.deleteById(id);
        }

        return "redirect:/user/viewmyfeedback";
    }

    @GetMapping("/user/feedback/edit/{id}")
    public String editFeedbackForm(@PathVariable Long id, HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";

        Optional<Feedback> feedback = feedbackRepository.findById(id);
        if (feedback.isPresent() &&
                feedback.get().getUsername().equals(session.getAttribute(SESSION_USERNAME))) {
            model.addAttribute("feedback", feedback.get());
            return "editfeedback";
        }

        return "redirect:/user/viewmyfeedback";
    }

    @PostMapping("/user/feedback/update")
    public String updateFeedback(@RequestParam Long id,
                                 @RequestParam String message,
                                 @RequestParam String feedbackType,
                                 @RequestParam Integer rating,
                                 HttpSession session) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";

        Optional<Feedback> feedback = feedbackRepository.findById(id);
        if (feedback.isPresent() &&
                feedback.get().getUsername().equals(session.getAttribute(SESSION_USERNAME))) {
            Feedback fb = feedback.get();
            fb.setMessage(message.trim());
            fb.setFeedbackType(feedbackType.trim());
            fb.setRating(rating);
            if (fb.getCreatedAt() == null) {
                fb.setCreatedAt(LocalDateTime.now());
            }
            feedbackRepository.save(fb);
        }

        return "redirect:/user/viewmyfeedback";
    }

    @GetMapping("/user/viewcomplaints")
    public String viewMyComplaints(HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";
        String username = (String) session.getAttribute(SESSION_USERNAME);
        model.addAttribute("feedbackList",
                feedbackRepository.findByUsernameAndFeedbackType(username, "Complaint"));
        return "viewcomplaints";
    }

    @GetMapping("/user/viewsuggestions")
    public String viewMySuggestions(HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";
        String username = (String) session.getAttribute(SESSION_USERNAME);
        model.addAttribute("feedbackList",
                feedbackRepository.findByUsernameAndFeedbackType(username, "Suggestion"));
        return "viewsuggestions";
    }

    @GetMapping("/user/viewpraising")
    public String viewMyPraising(HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "USER")) return "redirect:/login";
        String username = (String) session.getAttribute(SESSION_USERNAME);
        model.addAttribute("feedbackList",
                feedbackRepository.findByUsernameAndFeedbackType(username, "Praise"));
        return "viewpraising";
    }

    // ---------------- ADMIN SECTION ----------------

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        if (!isUserLoggedIn(session, "ADMIN")) return "redirect:/login";
        return "admindashboard";
    }

    @GetMapping("/admin/complaints")
    public String viewComplaints(HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "ADMIN")) return "redirect:/login";
        model.addAttribute("feedbacks", feedbackRepository.findByFeedbackType("Complaint"));
        return "complaints";
    }

    @GetMapping("/admin/suggestions")
    public String viewSuggestions(HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "ADMIN")) return "redirect:/login";
        model.addAttribute("feedbacks", feedbackRepository.findByFeedbackType("Suggestion"));
        return "suggestions";
    }

    @GetMapping("/admin/praising")
    public String viewPraising(HttpSession session, Model model) {
        if (!isUserLoggedIn(session, "ADMIN")) return "redirect:/login";
        model.addAttribute("feedbacks", feedbackRepository.findByFeedbackType("Praise"));
        return "praising";
    }

    @PostMapping("/admin/reply")
    public String replyToFeedback(@RequestParam Long feedbackId,
                                  @RequestParam String reply,
                                  HttpSession session) {
        if (!isUserLoggedIn(session, "ADMIN")) return "redirect:/login";

        Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
        if (feedback.isPresent()) {
            Feedback fb = feedback.get();
            fb.setReply(reply.trim());
            feedbackRepository.save(fb);
        }

        return "redirect:/admin/dashboard";
    }
}
