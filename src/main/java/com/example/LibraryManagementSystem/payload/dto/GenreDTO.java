package com.example.LibraryManagementSystem.payload.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreDTO {
    private Long id;

    @NotBlank(message = "Genre Code is Mandatory")
    @Schema(description = "Unique code for the genre", example = "SCI-FI", required = true)
    private String code;

    @NotBlank(message = "Genre Name is Mandatory")
    @Schema(description = "Name of the genre", example = "Science Fiction", required = true)
    private String name;

    @Size(max = 500, message = "Description can be at most 500 characters long")
    @Schema(description = "Detailed description of the genre", example = "Books featuring futuristic science and technology")
    private String description;

    @Min(value = 0, message = "Display Order must be non-negative")
    @Schema(description = "Order in which the genre should be displayed", example = "1", defaultValue = "0")


    private Integer displayOrder = 0;

    private Boolean active;

    private Long parentGenreId;

    private String parentGenreName;

    private List<GenreDTO> subGenres;

    private Long bookCount;

    private LocalDate createdAt;
    private LocalDate updatedAt;

}
