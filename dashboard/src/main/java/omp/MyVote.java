package omp;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="MyVote_table")
public class MyVote {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long voteId;
        private Long candidateId;
        private Long voterId;
        private Date votingDate;


        public Long getVoteId() {
            return voteId;
        }

        public void setVoteId(Long voteId) {
            this.voteId = voteId;
        }
        public Long getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(Long candidateId) {
            this.candidateId = candidateId;
        }
        public Long getVoterId() {
            return voterId;
        }

        public void setVoterId(Long voterId) {
            this.voterId = voterId;
        }
        public Date getVotingDate() {
            return votingDate;
        }

        public void setVotingDate(Date votingDate) {
            this.votingDate = votingDate;
        }

}
