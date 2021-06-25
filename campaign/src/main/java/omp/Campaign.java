package omp;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;

@Entity
@Table(name="Campaign_table")
public class Campaign {




    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long canditateId;
    private String theme;
    private String title;
    private String description;

    @PostPersist
    public void onPostPersist(){
        CampaignCreated campaignCreated = new CampaignCreated();
        BeanUtils.copyProperties(this, campaignCreated);
        campaignCreated.publishAfterCommit();


        CampaignUpdated campaignUpdated = new CampaignUpdated();
        BeanUtils.copyProperties(this, campaignUpdated);
        campaignUpdated.publishAfterCommit();


        CampaignDeleted campaignDeleted = new CampaignDeleted();
        BeanUtils.copyProperties(this, campaignDeleted);
        campaignDeleted.publishAfterCommit();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getCanditateId() {
        return canditateId;
    }

    public void setCanditateId(Long canditateId) {
        this.canditateId = canditateId;
    }
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
