package knu.intra.api;

import org.springframework.data.repository.CrudRepository;

public interface HarvRepository extends CrudRepository<Harv, Long> {
    Harv findById(String id);
    int deleteById(String id);
}
