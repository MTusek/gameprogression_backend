package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Game game = gameService.findById(id);
        if (game == null) {
            return ResponseEntity.notFound().build();
        }
        String coverUrl = game.getCoverUrl();
        if (!coverUrl.startsWith("http")) {
            coverUrl = "http://localhost:8080/images/" + coverUrl;
        }
        game.setCoverUrl(coverUrl);

        return ResponseEntity.ok(game);
    }

    // POST a new game
    @PostMapping
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    // DELETE a game
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    // Serve images in .png format
    @GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_PNG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = gameService.getImageAsStream(imageName);
        if (resource != null) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(resource, response.getOutputStream());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
        }
    }
}
