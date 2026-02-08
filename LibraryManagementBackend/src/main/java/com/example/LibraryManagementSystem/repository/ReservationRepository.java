package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Long>{

    @Query("select r from Reservation r where r.book.id = :bookId and r.status = 'PENDING' order by r.reservedAt")
    List<Reservation> findPendingReservationsByBook(@Param("bookId") Long bookId);

    @Query("select r from Reservation r where r.book.id = :bookId and r.status = 'PENDING' order by r.reservedAt limit 1")
    Optional<Reservation> findNextPendingReservation(@Param("bookId") Long bookId);

    @Query("select case when count(r) > 0 then true " +
            "else false end " +
            " from Reservation r " +
            "where r.users.id = :userId and r.book.id = :bookId " +
            "and (r.status = 'PENDING' or r.status = 'AVAILABLE')")
    boolean hasActiveReservation(@Param("userId") Long userId,@Param("bookId") Long bookId);

    @Query("select count(r) from Reservation r where r.users.id = :userId and (r.status = 'PENDING' or r.status = 'AVAILABLE')")
    long countActiveReservationsByUser(@Param("userId") Long userId);

    @Query("select count(r) from Reservation r where r.book.id = :bookId and r.status = 'PENDING'")
    long countPendingReservationByBook(@Param("bookId") Long bookId);

    @Query("select r from Reservation r where r.status = 'AVAILABLE' and r.availableUntil < :currentDateTime")
    List<Reservation> findExpiredReservations(@Param("currentDateTime") LocalDateTime currentDateTime);

    @Query("select r from Reservation r where r.users.id = :userId and r.book.id = :bookId and (r.status = 'AVAILABLE' or r.status = 'PENDING')")
    Optional<Reservation> findActiveReservationByUsersAndBook(@Param("userId") Long userId, @Param("bookId") Long bookId);
}
