package com.example.borrowingservice.repository;

import com.example.borrowingservice.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByUserId(Long userId);
    long countByUserIdAndReturnDateIsNull(Long userId); // Nombre d'emprunts actifs pour un utilisateur
}

