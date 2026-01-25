package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.exception.GenreException;
import com.example.LibraryManagementSystem.mapper.GenreMapper;
import com.example.LibraryManagementSystem.model.Genre;
import com.example.LibraryManagementSystem.payload.dto.GenreDTO;
import com.example.LibraryManagementSystem.repository.GenreRepository;
import com.example.LibraryManagementSystem.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreDTO createGenre(GenreDTO genreDTO) {

        Genre genre = genreMapper.toEntity(genreDTO);
        Genre savedGenre = genreRepository.save(genre);

        return genreMapper.toDto(savedGenre);
    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    public GenreDTO getGenreById(Long genreId) throws GenreException {
        Genre genre = genreRepository.findById(genreId).orElseThrow(() -> new GenreException("Genre not found"));
        return genreMapper.toDto(genre);
    }

    @Override
    public GenreDTO updateGenre(Long genreId, GenreDTO genre) {
        return null;
    }

    @Override
    public void deleteGenre(Long genreId) {

    }

    @Override
    public void hardDeleteGenre(Long genreId) {

    }

    @Override
    public List<GenreDTO> getAllActiveGenresWithSubGenres() {
        return List.of();
    }

    @Override
    public List<GenreDTO> getTopLevelGenres() {
        return List.of();
    }

    @Override
    public long getTotalActiveGenres() {
        return 0;
    }

    @Override
    public long getBookCountByGenreId(Long genreId) {
        return 0;
    }
}
