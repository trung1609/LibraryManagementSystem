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

    @GetMapping("/{genreId}")
    @Operation(
            summary = "Get genre by ID",
            description = "Retrieves a genre by its unique ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Genre.class))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> getGenreById(@PathVariable("genreId") long genreId) throws Exception {
        GenreDTO genreDTO = genreService.getGenreById(genreId);
        return ResponseEntity.ok(genreDTO);
    }

    @PutMapping("/{genreId}")
    @Operation(
            summary = "Update an existing genre",
            description = "Updates the details of an existing genre identified by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre updated successfully",
                    content = @Content(schema = @Schema(implementation = Genre.class))),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> updateGenre(@PathVariable("genreId") long genreId,
                                         @RequestBody GenreDTO genre) throws Exception {
        GenreDTO genreDTO = genreService.updateGenre(genreId, genre);
        return ResponseEntity.ok(genreDTO);
    }

    @DeleteMapping("/{genreId}")
    @Operation(
            summary = "Soft Delete a genre",
            description = "Soft deletes a genre by marking it as inactive."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> deleteGenre(@PathVariable("genreId") long genreId) throws Exception {
        genreService.deleteGenre(genreId);
        com.example.LibraryManagementSystem.payload.response.ApiResponse apiResponse = new com.example.LibraryManagementSystem.payload.response.ApiResponse("Genre deleted successfully - soft deleted", true);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/hard-delete/{genreId}")
    @Operation(
            summary = "Hard Delete a genre",
            description = "Permanently deletes a genre from the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre hard deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> hardDeleteGenre(@PathVariable("genreId") long genreId) {
        genreService.hardDeleteGenre(genreId);
        com.example.LibraryManagementSystem.payload.response.ApiResponse apiResponse = new com.example.LibraryManagementSystem.payload.response.ApiResponse("Genre hard deleted successfully", true);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/top-level")
    @Operation(
            summary = "Get top-level genres",
            description = "Retrieves a list of top-level genres (genres without parent genres) in the library system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of top-level genres retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Genre.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> getTopLevelGenres() {
        List<GenreDTO> genres = genreService.getTopLevelGenres();
        return ResponseEntity.ok(genres);
    }

    @GetMapping("/count")
    @Operation(
            summary = "Get total active genres",
            description = "Retrieves the total count of active genres in the library system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total active genres count retrieved successfully",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> getTotalActiveGenres() {
        long count = genreService.getTotalActiveGenres();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}/book-count")
    @Operation(
            summary = "Get book count by genre ID",
            description = "Retrieves the count of books associated with a specific genre ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book count retrieved successfully",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<?> getBookCountByGenreId(@PathVariable long id) {
        long count = genreService.getBookCountByGenreId(id);
        return ResponseEntity.ok(count);
    }
}
