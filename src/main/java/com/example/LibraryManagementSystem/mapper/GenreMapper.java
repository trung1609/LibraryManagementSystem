package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.Genre;
import com.example.LibraryManagementSystem.payload.dto.GenreDTO;
import com.example.LibraryManagementSystem.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreMapper {
    private final GenreRepository genreRepository;

    public GenreDTO toDto(Genre savedGenre) {
        if (savedGenre == null) {
            return null;
        }

        GenreDTO dto = GenreDTO.builder()
                .id(savedGenre.getId())
                .code(savedGenre.getCode())
                .name(savedGenre.getName())
                .description(savedGenre.getDescription())
                .displayOrder(savedGenre.getDisplayOrder())
                .active(savedGenre.getActive())
                .createdAt(savedGenre.getCreateAt())
                .updatedAt(savedGenre.getUpdateAt())
                .build();
        if (savedGenre.getParentGenre() != null) {
            dto.setParentGenreId(savedGenre.getParentGenre().getId());
            dto.setParentGenreName(savedGenre.getParentGenre().getName());
        }

        if (savedGenre.getSubGenres() != null && !savedGenre.getSubGenres().isEmpty()) {
            dto.setSubGenres(savedGenre.getSubGenres().stream()
                    .filter(Genre::getActive)
                    .map(this::toDto).toList());
        }
        return dto;
    }

    public Genre toEntity(GenreDTO genreDTO) {
        if (genreDTO == null) {
            return null;
        }

        Genre genre = Genre.builder()
                .code(genreDTO.getCode())
                .name(genreDTO.getName())
                .description(genreDTO.getDescription())
                .displayOrder(genreDTO.getDisplayOrder())
                .active(true)
                .build();

        if (genreDTO.getParentGenreId() != null) {
            genreRepository.findById(genreDTO.getParentGenreId()).ifPresent(genre::setParentGenre);
        }

        return genre;
    }

    public void updateEntityFromDto(GenreDTO genreDTO, Genre updatedGenre) {
        if (genreDTO == null) {
            return;
        }

        updatedGenre.setCode(genreDTO.getCode());
        updatedGenre.setName(genreDTO.getName());
        updatedGenre.setDescription(genreDTO.getDescription());
        updatedGenre.setDisplayOrder(genreDTO.getDisplayOrder() != null ? genreDTO.getDisplayOrder() : 0);
        if (genreDTO.getActive() != null) {
            updatedGenre.setActive(genreDTO.getActive());
        }

        if (genreDTO.getParentGenreId() != null) {
            genreRepository.findById(genreDTO.getParentGenreId()).ifPresent(updatedGenre::setParentGenre);
        }
    }

    public List<GenreDTO> toDTOList(List<Genre> genreList) {
        return genreList.stream().map(this::toDto).toList();
    }

}
