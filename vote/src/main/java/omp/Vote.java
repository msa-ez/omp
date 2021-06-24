package omp;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Vote_table")
public class Vote {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long candidateId;
    private String voterId;
    private Date votingDate;

    @PostPersist
    public void onPostPersist(){
        Voted voted = new Voted();
        BeanUtils.copyProperties(this, voted);
        voted.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        omp.external.Election election = new omp.external.Election();
        // mappings goes here
        Application.applicationContext.getBean(omp.external.ElectionService.class)
            .canVote(election);


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
