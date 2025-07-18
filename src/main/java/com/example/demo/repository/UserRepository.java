package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // Custom method to find user by username
    Optional<User> findByUsername(String username);
    
    // Optionally, you can add more methods if needed in future
    // Optional<User> findByUsernameAndPassword(String username, String password);
}
