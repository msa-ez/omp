package omp;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Candidate_table")
public class Candidate {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long elctionId;
    private Integer no;
    private String party;
    private String name;
    private Integer campaigns;

    @PostPersist
    public void onPostPersist(){
        CandidateCreated candidateCreated = new CandidateCreated();
        BeanUtils.copyProperties(this, candidateCreated);
        candidateCreated.publishAfterCommit();


        CandidateUpdated candidateUpdated = new CandidateUpdated();
        BeanUtils.copyProperties(this, candidateUpdated);
        candidateUpdated.publishAfterCommit();


        CandidateDeleted candidateDeleted = new CandidateDeleted();
        BeanUtils.copyProperties(this, candidateDeleted);
        candidateDeleted.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getElctionId() {
        return elctionId;
    }

    public void setElctionId(Long elctionId) {
        this.elctionId = elctionId;
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


    public void increaseCampaigns(){
        if(this.campaigns == null) this.campaigns = 0;
        this.setCampaigns(campaigns + 1);
    }

    public void decreaseCampaigns(){
        if(this.campaigns == null) this.campaigns = 0;
        int value = this.campaigns - 1;
        this.setCampaigns(value < 0 ? 0 : value);
    }


}
