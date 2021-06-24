package omp;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Vote")
public class Vote {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long electionId;
    private Long candidateId;
    private String voterId;
    private Date votingDate;

    @PostPersist
    public void onPostPersist() throws Exception {

        // 선거 가능 기간 여부 확인
        if(VoteApplication.applicationContext.getBean(omp.external.ElectionService.class)
            .canVote(electionId)){
                Voted voted = new Voted();
                BeanUtils.copyProperties(this, voted);
                voted.publishAfterCommit();
            }else{
                throw new Exception("Not Voting Day.");
            }


    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getElectionId() {
        return electionId;
    }
    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }
    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }
    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }
    public Date getVotingDate() {
        return votingDate;
    }

    public void setVotingDate(Date votingDate) {
        this.votingDate = votingDate;
    }
}
