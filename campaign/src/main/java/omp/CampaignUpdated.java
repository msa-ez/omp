package omp;

public class CampaignUpdated extends AbstractEvent {

    private Long id;
    private Long canditateId;
    private String theme;
    private String title;
    private String description;

    public CampaignUpdated(){
        super();
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
