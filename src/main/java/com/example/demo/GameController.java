package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // GET all games
    @GetMapping
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    // GET a game by ID
    @GetMapping("/{id}")
    public Optional<Game> getGameById(@PathVariable Long id) {
        return gameService.getGameById(id);
    }

    // POST a new game
    @PostMapping
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    // PUT (update) an existing game
    @PutMapping("/{id}")
    public Game updateGame(@PathVariable Long id, @RequestBody Game updatedGame) {
        Optional<Game> existingGame = gameService.getGameById(id);
        if (existingGame.isPresent()) {
            updatedGame.setId(id);
            return gameService.saveGame(updatedGame);
        }
        throw new RuntimeException("Game not found with ID: " + id);
    }

    // DELETE a game
    @DeleteMapping("/{id}")
    public void deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
    }
}

