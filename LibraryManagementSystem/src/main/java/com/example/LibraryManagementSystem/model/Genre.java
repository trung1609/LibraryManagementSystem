package com.example.LibraryManagementSystem.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Genre entity representing a book category or classification")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the genre", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
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

    @Column(nullable = false)
    @Schema(description = "Whether the genre is active", example = "true", defaultValue = "true")
    private Boolean active = true;

    @ManyToOne
    @Schema(description = "Parent genre if this is a sub-genre")
    private Genre parentGenre;

    @OneToMany
    @Schema(description = "List of sub-genres under this genre")
    private List<Genre> subGenres = new ArrayList<>();

//    @OneToMany(mappedBy = "genre", cascade = CascadeType.PERSIST)
//    private List<Book> books = new ArrayList<>();

    @CreationTimestamp
    @Schema(description = "Date when the genre was created", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Schema(description = "Date when the genre was last updated", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateAt;
}
