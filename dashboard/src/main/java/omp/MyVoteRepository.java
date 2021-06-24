package omp;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyVoteRepository extends CrudRepository<MyVote, Long> {


        void deleteByCandidateId(Long candidateId);
}