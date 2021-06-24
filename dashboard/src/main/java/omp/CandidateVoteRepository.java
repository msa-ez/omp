package omp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateVoteRepository extends CrudRepository<CandidateVote, Long> {


}