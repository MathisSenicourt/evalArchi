package com.example.bookservice.service;

import com.example.bookservice.entity.Book;
import java.util.List;
public interface BookService {
    List<Book> getAllBooks();

    Book getBookById(Long id);

    Book createBook(Book book);

    Book updateBook(Long id, Book book);

    void deleteBook(Long id);

    boolean borrowBook(Long id);

    boolean returnBook(Long id);
}