package omp;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="campaigns", path="campaigns")
public interface CampaignRepository extends PagingAndSortingRepository<Campaign, Long>{


}
