package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.model.Genre;
import com.example.LibraryManagementSystem.payload.dto.GenreDTO;
import com.example.LibraryManagementSystem.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
@Tag(name = "Genre", description = "Genre management APIs")
public class GenreController {
    private final GenreService genreService;

    @PostMapping("/create")
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDTO) {
        GenreDTO createdGenre = genreService.createGenre(genreDTO);
        return ResponseEntity.ok(createdGenre);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<?> getGenreById(@PathVariable("genreId") long genreId) throws Exception {
        GenreDTO genreDTO = genreService.getGenreById(genreId);
        return ResponseEntity.ok(genreDTO);
    }

    @PutMapping("/{genreId}")
    public ResponseEntity<?> updateGenre(@PathVariable("genreId") long genreId,
                                         @RequestBody GenreDTO genre) throws Exception {
        GenreDTO genreDTO = genreService.updateGenre(genreId, genre);
        return ResponseEntity.ok(genreDTO);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> deleteGenre(@PathVariable("genreId") long genreId) throws Exception {
        genreService.deleteGenre(genreId);
        com.example.LibraryManagementSystem.payload.response.ApiResponse apiResponse = new com.example.LibraryManagementSystem.payload.response.ApiResponse("Genre deleted successfully - soft deleted", true);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/hard-delete/{genreId}")
    public ResponseEntity<?> hardDeleteGenre(@PathVariable("genreId") long genreId) {
        genreService.hardDeleteGenre(genreId);
        com.example.LibraryManagementSystem.payload.response.ApiResponse apiResponse = new com.example.LibraryManagementSystem.payload.response.ApiResponse("Genre hard deleted successfully", true);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/top-level")
    public ResponseEntity<?> getTopLevelGenres() {
        List<GenreDTO> genres = genreService.getTopLevelGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getTotalActiveGenres() {
        long count = genreService.getTotalActiveGenres();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/book-count")
    public ResponseEntity<?> getBookCountByGenreId(@PathVariable long id) {
        long count = genreService.getBookCountByGenreId(id);
        return ResponseEntity.ok(count);
    }
}
