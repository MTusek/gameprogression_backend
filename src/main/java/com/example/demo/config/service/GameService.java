package com.example.demo.config.service;

import com.example.demo.config.repo.GameRepository;
import com.example.demo.config.model.Game;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Game findById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public String getImageAsBase64(String imageName) {
        try (InputStream resource = getImageAsStream(imageName)) {
            if (resource != null) {
                byte[] imageBytes = resource.readAllBytes();
                return "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public InputStream getImageAsStream(String imageName) {
        try {
            return getClass().getResourceAsStream("/static/images/" + imageName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
