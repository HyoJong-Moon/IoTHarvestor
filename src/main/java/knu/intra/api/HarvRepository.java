package knu.intra.api;

import org.springframework.data.repository.CrudRepository;

public interface HarvRepository extends CrudRepository<Harv, Long> {
    Harv findByHarvestId(String harvestId);
    int deleteByHarvestId(String harvestId);
}
