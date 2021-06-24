package omp;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="elections", path="elections")
public interface ElectionRepository extends PagingAndSortingRepository<Election, Long>{


}
