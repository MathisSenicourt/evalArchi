package com.example.borrowingservice.service;

import com.example.borrowingservice.entity.Borrowing;

import java.util.List;

public interface BorrowingService {

    Borrowing createBorrowing(Long userId, Long bookId);

    void returnBorrowing(Long id);

    List<Borrowing> getBorrowingsByUserId(Long userId);
}
