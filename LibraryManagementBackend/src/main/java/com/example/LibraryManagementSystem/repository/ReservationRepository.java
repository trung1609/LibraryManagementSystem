package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Long>{

}
