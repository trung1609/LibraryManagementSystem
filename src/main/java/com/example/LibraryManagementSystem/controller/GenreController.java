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

    @Operation(
            summary = "Create a new genre",
            description = "Creates a new genre in the library system. Returns the created genre with generated ID and timestamps."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre created successfully",
                    content = @Content(schema = @Schema(implementation = Genre.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<GenreDTO> addGenre(@RequestBody GenreDTO genreDTO) {
        GenreDTO createdGenre = genreService.createGenre(genreDTO);
        return ResponseEntity.ok(createdGenre);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all genres",
            description = "Retrieves a list of all genres in the library system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of genres retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Genre.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }
}
