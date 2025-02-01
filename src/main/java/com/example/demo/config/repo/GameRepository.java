package com.example.demo.config.repo;

import com.example.demo.config.model.Game;
import com.example.demo.config.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByUser(User user);
}
