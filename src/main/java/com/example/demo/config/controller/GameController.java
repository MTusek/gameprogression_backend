package com.example.demo.config.controller;

import com.example.demo.config.service.GameService;
import com.example.demo.config.model.Game;
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
    @GetMapping
    public ResponseEntity<String> getAllGamesAsHtml() {
        List<Game> games = gameService.getAllGames();

        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html>");
        htmlBuilder.append("<body>");
        htmlBuilder.append("<h1>Game List</h1>");
        htmlBuilder.append("<ul>");

        for (Game game : games) {
            htmlBuilder.append("<li>");
            htmlBuilder.append("<h2>").append(game.getName()).append("</h2>");
            htmlBuilder.append("<p>").append(game.getDescription()).append("</p>");
            htmlBuilder.append("<img src=\"")
                    .append(game.getCoverUrl())
                    .append("\" alt=\"")
                    .append(game.getName().toLowerCase())
                    .append("\" style=\"max-width:200px; height:auto;\"/>");
            htmlBuilder.append("</li>");
        }

        htmlBuilder.append("</ul>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(htmlBuilder.toString());
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
