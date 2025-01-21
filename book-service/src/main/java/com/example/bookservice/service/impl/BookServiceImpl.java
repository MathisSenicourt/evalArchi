package com.example.bookservice.service.impl;

import com.example.bookservice.entity.Book;
import com.example.bookservice.repository.BookRepository;
import com.example.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book createBook(Book book) {
        book.setAvailable(true); // Un livre est disponible par défaut à sa création
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setCategory(updatedBook.getCategory());
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    @Override
    public boolean borrowBook(Long id) {
        Book book = getBookById(id);
        if (!book.isAvailable()) {
            throw new RuntimeException("Book is not available for borrowing.");
        }
        book.setAvailable(false); // Rendre le livre indisponible
        bookRepository.save(book);
        return true;
    }

    @Override
    public boolean returnBook(Long id) {
        Book book = getBookById(id);
        if (book.isAvailable()) {
            throw new RuntimeException("Book is already available.");
        }
        book.setAvailable(true); // Rendre le livre disponible
        bookRepository.save(book);
        return true;
    }
}
