package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.Genre;
import com.example.LibraryManagementSystem.payload.dto.BookDTO;
import com.example.LibraryManagementSystem.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final GenreRepository genreRepository;

    public BookDTO toDTO(Book book) {
        if (book == null) {
            return null;
        }
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genreId(book.getGenre().getId())
                .genreName(book.getGenre().getName())
                .genreCode(book.getGenre().getCode())
                .publisher(book.getPublisher())
                .publicationDate(book.getPublicationDate())
                .language(book.getLanguage())
                .description(book.getDescription())
                .pages(book.getPages())
                .availableCopies(book.getAvailableCopies())
                .totalCopies(book.getTotalCopies())
                .active(book.getActive())
                .price(book.getPrice())
                .coverImageUrl(book.getCoverImageUrl())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    public Book toEntity(BookDTO bookDTO) throws BookException {
        if (bookDTO == null) {
            return null;
        }

        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setIsbn(bookDTO.getIsbn());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());

        if (bookDTO.getGenreId() != null) {
            Genre genre = genreRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(() -> new BookException("Genre not found with id: " + bookDTO.getGenreId()));
            book.setGenre(genre);
        }
        book.setPublisher(bookDTO.getPublisher());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setLanguage(bookDTO.getLanguage());
        book.setPages(bookDTO.getPages());
        book.setDescription(bookDTO.getDescription());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setPrice(bookDTO.getPrice());
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());
        book.setActive(true);
        return book;
    }

    public void updateEntityFromDTO(BookDTO bookDTO, Book book) throws BookException {
        if (bookDTO == null || book == null) {
            return;
        }
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());

        if (bookDTO.getGenreId() != null) {
            Genre genre = genreRepository.findById(bookDTO.getGenreId())
                    .orElseThrow(() -> new BookException("Genre not found with id: " + bookDTO.getGenreId()));
            book.setGenre(genre);
        }

        book.setPublisher(bookDTO.getPublisher());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setLanguage(bookDTO.getLanguage());
        book.setPages(bookDTO.getPages());
        book.setDescription(bookDTO.getDescription());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setPrice(bookDTO.getPrice());
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());
        if (bookDTO.getActive() != null) {
            book.setActive(bookDTO.getActive());
        }
    }
}
