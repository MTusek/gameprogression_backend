package com.example.demo.config.controller;

import com.example.demo.config.model.Game;
import com.example.demo.config.service.GameService;
import com.example.demo.config.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    // Get all games for the logged-in user
    @GetMapping
    public ResponseEntity<List<Game>> getUserGames(@AuthenticationPrincipal UserDetails currentUser) {
        List<Game> games = userService.getUserGames(currentUser.getUsername());
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        Game game = userService.getUserGameById(currentUser.getUsername(), id);
        return game != null ? ResponseEntity.ok(game) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Game> addGame(@RequestBody Game game, @AuthenticationPrincipal UserDetails currentUser) {
        Game savedGame = userService.addGameToUser(currentUser.getUsername(), game);
        return ResponseEntity.ok(savedGame);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id, @AuthenticationPrincipal UserDetails currentUser) {
        boolean deleted = userService.deleteUserGame(currentUser.getUsername(), id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
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
