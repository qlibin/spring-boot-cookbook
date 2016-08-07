package me.qlibin.repository;

import me.qlibin.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    public Book findBookByIsbn(String isbn);
}
