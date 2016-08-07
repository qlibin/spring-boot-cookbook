package me.qlibin;

import me.qlibin.entity.Author;
import me.qlibin.entity.Book;
import me.qlibin.entity.Publisher;
import me.qlibin.repository.AuthorRepository;
import me.qlibin.repository.BookRepository;
import me.qlibin.repository.PublisherRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Command line runners are a useful functionality to execute the various types of code that only have to be run once,
 * right after application startup. Some may also use this as a place to start various executor threads
 * but Spring Boot provides a better solution for this task, which will be discussed at the end of this chapter.
 * The CommandLineRunner interface is used by Spring Boot to scan all of its implementations and invoke each instance's
 * run method with the startup arguments. We can also use an @Order annotation or implement an Ordered interface
 * so as to define the exact order in which we want Spring Boot to execute them. For example,
 * Spring Batch relies on the runners in order to trigger the execution of the jobs.
 *
 * As command-line runners are instantiated and executed after the application has started, we can use the dependency
 * injection to our advantage in order to wire in whatever dependencies that we need, such as data sources, services,
 * and other components. These can be utilized later while implementing run(String... args) method..
 *
 * Note
 * It is important to note that if any exceptions are thrown inside the run(String… args) method,
 * this will cause the context to close and an application to shut down.
 * Wrapping the risky code blocks with try/catch is recommended in order to prevent this from happening.
 */
public class StartupRunner implements CommandLineRunner {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private PublisherRepository publisherRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Number of books: " + bookRepository.count());
        Author author = new Author("Alex", "Antonov");
        author = authorRepository.save(author);
        Publisher publisher = new Publisher("Packt");
        publisher = publisherRepository.save(publisher);
        Book book = new Book("978-1-78528-415-1", "Spring Boot Recipes", author, publisher);
        bookRepository.save(book);
    }

    /**
     * @EnableScheduling is not a Spring Boot annotation, but instead is a Spring Context module annotation.
     * Similar to the @SpringBootApplication and @EnableAutoConfiguration annotations, this is a meta-annotation
     * and internally imports the SchedulingConfiguration via the @Import(SchedulingConfiguration.class) instruction,
     * which can be seen if looked found inside the code for the @EnableScheduling annotation class.
     *
     * ScheduledAnnotationBeanPostProcessor that will be created by the imported configuration will scan the declared
     * Spring Beans for the presence of the @Scheduled annotations. For every annotated method without arguments,
     * the appropriate executor thread pool will be created.
     * It will manage the scheduled invocation of the annotated method.
     */
    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void run() {
        logger.info("Number of books: " + bookRepository.count());
    }

}
