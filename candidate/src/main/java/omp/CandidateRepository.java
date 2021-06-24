package omp;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="candidates", path="candidates")
public interface CandidateRepository extends PagingAndSortingRepository<Candidate, Long>{


}
