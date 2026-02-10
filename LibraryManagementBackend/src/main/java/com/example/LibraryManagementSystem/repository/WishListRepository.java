package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    Page<WishList> findByUsersId(Long userId, Pageable pageable);
    WishList findByUsersIdAndBookId(Long userId, Long bookId);
    boolean existsByUsersIdAndBookId(Long userId, Long bookId);
}
