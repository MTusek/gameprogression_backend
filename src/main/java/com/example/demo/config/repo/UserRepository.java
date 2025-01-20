package com.example.demo.config.repo;

import com.example.demo.config.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByUsername(String username);
}
