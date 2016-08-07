package me.qlibin.controllers;

import me.qlibin.entity.Book;
import me.qlibin.entity.Reviewer;
import me.qlibin.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @RequestMapping(value = "/{isbn}", method =
            RequestMethod.GET)
    public Book getBook(@PathVariable Isbn isbn) {
        return bookRepository.findBookByIsbn(isbn.getIsbn());
    }

    public class Isbn {
        private String isbn;

        public Isbn(String isbn) {
            this.isbn = isbn;
        }

        public String getIsbn() {
            return isbn;
        }
    }

    public class IsbnEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (StringUtils.hasText(text)) {
                setValue(new Isbn(text.trim()));
            }
            else {
                setValue(null);
            }
        }

        @Override
        public String getAsText() {
            Isbn isbn = (Isbn) getValue();
            if (isbn != null) {
                return isbn.getIsbn();
            }
            else {
                return "";
            }
        }
    }

    /**
     * Spring automatically configures a large number of default editors; but for custom types,
     * we have to explicitly instantiate new editors for every web request.
     * This is done in the controller in a method that is annotated with @InitBinder.
     * This annotation is scanned and all the detected methods should have a signature of accepting WebDataBinder
     * as an argument. Among other things, WebDataBinder provides us with an ability to register as many custom editors
     * as we require for the controller methods to be bound properly.
     *
     * Note:
     * It is VERY important to know that PropertyEditor is not thread safe!
     * For this reason, we have to create a new instance of our custom editors
     * for every web request and register them with WebDataBinder.
     *
     * In case a new PropertyEditor is needed, it is best to create one by extending PropertyEditorSupport
     * and overriding the desired methods with custom implementation.
     *
     * Mostly because of its statefulness and lack of thread safety, since version 3,
     * Spring has added a Formatter interface as a replacement for PropertyEditor.
     * The Formatters are intended to provide a similar functionality but in a completely thread-safe manner
     * and focusing on a very specific task of parsing a String in an object type
     * and converting an object to its String representation.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Isbn.class, new IsbnEditor());
    }

    @RequestMapping(value = "/{isbn}/reviewers", method = RequestMethod.GET)
    public List<Reviewer> getReviewers(@PathVariable("isbn") Book book) {
        return book.getReviewers();
    }

}
