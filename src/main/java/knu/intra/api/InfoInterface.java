package knu.intra.api;

import org.springframework.data.repository.CrudRepository;

public interface InfoInterface extends CrudRepository<Info, Long> {
    Info findById(String id);
    int deleteById(String id);
}
