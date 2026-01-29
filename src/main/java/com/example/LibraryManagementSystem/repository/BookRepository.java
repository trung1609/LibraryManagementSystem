package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    @Query(value = """
            SELECT * FROM book b
            WHERE
                (:searchTerm is null or
                 lower(b.title) like concat('%', lower(cast(:searchTerm as char)), '%') or
                 lower(b.author) like concat('%', lower(cast(:searchTerm as char)), '%') or
                 lower(b.isbn) like concat('%', lower(cast(:searchTerm as char)), '%'))
                and (:genreId is null or b.genre_id = :genreId)
                and (:availableOnly = false or b.available_copies > 0)
                and b.active = true
            """,
            countQuery = """
                    SELECT count(*) FROM book b
                    WHERE
                        (:searchTerm is null or
                         lower(b.title) like concat('%', lower(cast(:searchTerm as char)), '%') or
                         lower(b.author) like concat('%', lower(cast(:searchTerm as char)), '%') or
                         lower(b.isbn) like concat('%', lower(cast(:searchTerm as char)), '%'))
                        and (:genreId is null or b.genre_id = :genreId)
                        and (:availableOnly = false or b.available_copies > 0)
                        and b.active = true
                    """,
            nativeQuery = true)
    Page<Book> searchBooksWithFiltersNative(
            @Param("searchTerm") String searchTerm,
            @Param("genreId") Long genreId,
            @Param("availableOnly") boolean availableOnly,
            Pageable pageable
    );

    long countByActiveTrue();

    @Query("select  count(b) from Book b where b.availableCopies > 0 and b.active = true ")
    long countAvailableBooks();
}
