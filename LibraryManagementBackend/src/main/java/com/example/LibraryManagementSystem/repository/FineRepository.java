package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.domain.FineStatus;
import com.example.LibraryManagementSystem.domain.FineType;
import com.example.LibraryManagementSystem.model.Fine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    @Query("select f from Fine f " +
            "where (:userId is null or f.users.id = :userId )" +
            "and (:status is null or f.status = :status )" +
            "and (:type is null or f.type = :type) " +
            "order by f.createdAt desc")
    Page<Fine> findAllWithFilters(
            @Param("userId") Long userId,
            @Param("status") FineStatus status,
            @Param("type") FineType type,
            Pageable pageable
    );

    List<Fine> findByUsersId(Long userId);

    List<Fine> findByUsersIdAndType(Long userId, FineType type);
}
