package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Suggestion;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
}
