package me.qlibin.formatters;

import me.qlibin.entity.Book;
import me.qlibin.repository.BookRepository;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;

import java.util.Locale;

public class BookFormatter implements Formatter<Book> {
    private BookRepository repository;
    public BookFormatter(BookRepository repository) {
        this.repository = repository;
    }
    @Override
    public Book parse(String bookIdentifier, Locale locale) throws ParseException {
        Book book = repository.findBookByIsbn(bookIdentifier);
        return book != null ? book : repository.findOne(Long.valueOf(bookIdentifier));
    }
    @Override
    public String print(Book book, Locale locale) {
        return book.getIsbn();
    }
}

