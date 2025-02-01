package com.example.demo.config.service;

import com.example.demo.config.model.Game;
import com.example.demo.config.model.User;
import com.example.demo.config.model.UserRegistration;
import com.example.demo.config.repo.GameRepository;
import com.example.demo.config.repo.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private  GameRepository gameRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.gameRepository = gameRepository;
    }

    public void registerUser(UserRegistration registration) {
        if (registration.getUsername() == null || registration.getPassword() == null || registration.getEmail() == null) {
            throw new IllegalArgumentException("All fields are required");
        }

        User user = new User();
        user.setUsername(registration.getUsername());
        user.setPassword(registration.getPassword());
        user.setEmail(registration.getEmail());
        user.setRoles("USER");
        user.setEnabled(true);
        userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<Game> getUserGames(String username) {
        User user = userRepository.findByUsername(username);
        return user.getGames();

    }

    public Game getUserGameById(String username, Long id) {
        User user = userRepository.findByUsername(username);
        return user.getGames().stream()
                .filter(game -> game.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Game addGameToUser(String username, Game game) {
        User user = userRepository.findByUsername(username);
        game.setUser(user);
        Game savedGame = gameRepository.save(game);
        user.getGames().add(savedGame);
        userRepository.save(user);
        return savedGame;
    }

    public boolean deleteUserGame(String username, Long id) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        Optional<Game> gameToDelete = user.getGames().stream()
                .filter(game -> game.getId().equals(id))
                .findFirst();

        if (gameToDelete.isPresent()) {
            user.getGames().remove(gameToDelete.get());
            gameRepository.delete(gameToDelete.get());
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
