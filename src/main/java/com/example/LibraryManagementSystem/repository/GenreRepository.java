package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIdAndActiveTrueOrderByDisplayOrderAsc(Long parentGenreId);

    long countByActiveTrue();

//    @Query("select count(b) from book b where b.genre.id =: genreId")
//    long countBookByGenre(@Param("genreId") Long genreId);

}
