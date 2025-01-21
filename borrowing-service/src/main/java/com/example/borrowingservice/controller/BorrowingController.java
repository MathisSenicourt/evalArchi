package com.example.borrowingservice.controller;

import com.example.borrowingservice.entity.Borrowing;
import com.example.borrowingservice.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    @Autowired
    private BorrowingService borrowingService;

    @PostMapping
    public ResponseEntity<Borrowing> createBorrowing(@RequestParam Long userId, @RequestParam Long bookId) {
        Borrowing borrowing = borrowingService.createBorrowing(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(borrowing);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<String> returnBorrowing(@PathVariable Long id) {
        borrowingService.returnBorrowing(id);
        return ResponseEntity.ok("Borrowing returned successfully.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Borrowing>> getBorrowingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(borrowingService.getBorrowingsByUserId(userId));
    }
}