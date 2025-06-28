package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.demo.model.Suggestion;
import com.example.demo.repository.SuggestionRepository;

@Service
public class SuggestionServiceImpl implements SuggestionService {

    @Autowired
    private SuggestionRepository suggestionRepository;

    @Override
    public List<Suggestion> getAllSuggestions() {
        return suggestionRepository.findAll();
    }
}
