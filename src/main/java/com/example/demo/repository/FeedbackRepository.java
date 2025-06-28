package com.example.demo.repository;

import com.example.demo.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for accessing Feedback data from the database.
 * Extends JpaRepository to provide CRUD operations and custom queries.
 */
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    /**
     * Find all feedback entries with a specific feedback type.
     * 
     * @param feedbackType the type of feedback (e.g., Complaint, Suggestion, Praise)
     * @return a list of feedbacks matching the given type
     */
    List<Feedback> findByFeedbackType(String feedbackType);

    /**
     * Find all feedback entries submitted by a specific user.
     * 
     * @param username the username of the feedback submitter
     * @return a list of feedbacks submitted by the given user
     */
    List<Feedback> findByUsername(String username);

    /**
     * Find all feedback entries of a specific type submitted by a specific user.
     * 
     * @param username the username of the feedback submitter
     * @param feedbackType the type of feedback
     * @return a list of feedbacks matching the username and feedback type
     */
    List<Feedback> findByUsernameAndFeedbackType(String username, String feedbackType);
}
