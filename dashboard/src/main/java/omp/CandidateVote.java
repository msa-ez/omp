package omp;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="CandidateVote_table")
public class CandidateVote {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long candidateId;
        private Long electionId;
        private Integer no;
        private String party;
        private String name;
        private Integer campaigns;
        private Integer votes;


        public Long getCandidateId() {
            return candidateId;
        }

        public void setCandidateId(Long candidateId) {
            this.candidateId = candidateId;
        }
        public Long getElectionId() {
            return electionId;
        }

        public void setElectionId(Long electionId) {
            this.electionId = electionId;
        }
        public Integer getNo() {
            return no;
        }

        public void setNo(Integer no) {
            this.no = no;
        }
        public String getParty() {
            return party;
        }

        public void setParty(String party) {
            this.party = party;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        public Integer getCampaigns() {
            return campaigns;
        }

        public void setCampaigns(Integer campaigns) {
            this.campaigns = campaigns;
        }
        public Integer getVotes() {
            return votes;
        }

        public void setVotes(Integer votes) {
            this.votes = votes;
        }

}
