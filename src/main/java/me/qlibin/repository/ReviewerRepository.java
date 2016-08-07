package me.qlibin.repository;

import me.qlibin.entity.Reviewer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ReviewerRepository extends
        PagingAndSortingRepository<Reviewer, Long> {
}

