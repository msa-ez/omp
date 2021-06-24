package omp;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Election_table")
public class Election {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private Date votingDay;
    private Integer votingPeriod;

    @PostPersist
    public void onPostPersist(){
        ElectionCreated electionCreated = new ElectionCreated();
        BeanUtils.copyProperties(this, electionCreated);
        electionCreated.publishAfterCommit();


        ElectionUpdated electionUpdated = new ElectionUpdated();
        BeanUtils.copyProperties(this, electionUpdated);
        electionUpdated.publishAfterCommit();


        ElectionDeleted electionDeleted = new ElectionDeleted();
        BeanUtils.copyProperties(this, electionDeleted);
        electionDeleted.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Date getVotingDay() {
        return votingDay;
    }

    public void setVotingDay(Date votingDay) {
        this.votingDay = votingDay;
    }
    public Integer getVotingPeriod() {
        return votingPeriod;
    }

    public void setVotingPeriod(Integer votingPeriod) {
        this.votingPeriod = votingPeriod;
    }




}
