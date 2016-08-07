package me.qlibin.repository;

import me.qlibin.entity.Publisher;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PublisherRepository extends
        PagingAndSortingRepository<Publisher, Long> {
}

