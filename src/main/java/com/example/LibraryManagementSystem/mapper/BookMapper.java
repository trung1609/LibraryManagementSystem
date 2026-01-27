package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.payload.dto.BookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {
    public BookDTO toDTO(Book book) {
        if(book == null) {
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
}
