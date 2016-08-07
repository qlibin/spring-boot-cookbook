package me.qlibin.repository;

import me.qlibin.entity.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The reason that we've decided to do this is to get the extra benefits provided by the PagingAndSortingRepository.
 * This will add the extra functionality to retrieve entities using the pagination and being able to sort them.
 *
 * The @RepositoryRestResource annotation, while optional, provides us with the ability to have a finer control
 * over the exposure of the repository as a web data service. For example, if we wanted to change the URL path
 * or rel value, to writers instead of authors, we could have tuned the annotation, as follows:
 *
 * @RepositoryRestResource(collectionResourceRel = "writers", path = "writers")
 *
 * As we included spring-boot-starter-data-rest in our build dependencies, we will also get the spring-hateoas
 * library support, which gives us nice ALPS metadata, such as a _links object. This can be very helpful
 * when building an API-driven UI, which can deduce the navigational capabilities from the metadata
 * and present them appropriately.
 */
@RepositoryRestResource
public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
}
