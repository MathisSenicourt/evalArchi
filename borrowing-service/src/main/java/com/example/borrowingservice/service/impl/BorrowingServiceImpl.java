package com.example.borrowingservice.service.impl;

import com.example.bookservice.entity.Book;
import com.example.bookservice.service.BookService;
import com.example.borrowingservice.entity.Borrowing;
import com.example.borrowingservice.repository.BorrowingRepository;
import com.example.borrowingservice.service.BorrowingService;
import com.example.userservice.entity.MembershipType;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingServiceImpl implements BorrowingService {

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8090/api/users";
    private static final String BOOK_SERVICE_URL = "http://localhost:8090/api/books";

    @Override
    public Borrowing createBorrowing(Long userId, Long bookId) {
        // Récupérer l'utilisateur depuis le microservice User
        User user = restTemplate.getForObject(USER_SERVICE_URL + "/" + userId, User.class);
        if (user.isLocked()) {
            throw new RuntimeException("User is locked and cannot borrow more books.");
        }

        // Récupérer le livre depuis le microservice Book
        Book book = restTemplate.getForObject(BOOK_SERVICE_URL + "/" + bookId, Book.class);
        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available for borrowing.");
        }

        // Enregistrer l'emprunt
        Borrowing borrowing = new Borrowing();
        borrowing.setUserId(userId);
        borrowing.setBookId(bookId);
        borrowing.setBorrowDate(LocalDate.now());
        borrowing.setReturnDate(null); // Pas encore retourné
        borrowingRepository.save(borrowing);

        // Mettre à jour la disponibilité du livre
        book.setAvailable(false);
        restTemplate.put(BOOK_SERVICE_URL + "/" + bookId, book);

        // Mettre à jour le statut de l'utilisateur
        int maxBorrows = user.getMembershipType() == MembershipType.PREMIUM ? 7 : 5;
        long currentBorrows = borrowingRepository.countByUserIdAndReturnDateIsNull(userId);
        if (currentBorrows >= maxBorrows) {
            user.setLocked(true);
        }
        restTemplate.put(USER_SERVICE_URL + "/" + userId, user);

        return borrowing;
    }

    @Override
    public void returnBorrowing(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new RuntimeException("Borrowing not found"));

        // Rendre le livre disponible
        Book book = restTemplate.getForObject(BOOK_SERVICE_URL + "/" + borrowing.getBookId(), Book.class);
        book.setAvailable(true);
        restTemplate.put(BOOK_SERVICE_URL + "/" + book.getId(), book);

        // Marquer l'emprunt comme retourné
        borrowing.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowing);

        // Mettre à jour le statut de l'utilisateur
        User user = restTemplate.getForObject(USER_SERVICE_URL + "/" + borrowing.getUserId(), User.class);
        user.setLocked(false); // Débloquer l'utilisateur si applicable
        restTemplate.put(USER_SERVICE_URL + "/" + user.getId(), user);
    }

    @Override
    public List<Borrowing> getBorrowingsByUserId(Long userId) {
        return borrowingRepository.findByUserId(userId);
    }
}
