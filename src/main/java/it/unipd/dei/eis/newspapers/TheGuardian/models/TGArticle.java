package it.unipd.dei.eis.newspapers.TheGuardian.models;

import java.time.Instant;

public class TGArticle {
    private String id;
    private String type;
    private String sectionId;
    private String sectionName;
    private Instant webPublicationDate;
    private String webTitle;
    private String webUrl;
    private String apiUrl;
    private TGFields fields;
    private boolean isHosted;
    private String pillarId;
    private String pillarName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Instant getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(Instant webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public TGFields getFields() {
        return fields;
    }

    public void setFields(TGFields fields) {
        this.fields = fields;
    }

    public boolean getIsHosted() {
        return isHosted;
    }

    public void setHosted(boolean hosted) {
        isHosted = hosted;
    }

    public String getPillarId() {
        return pillarId;
    }

    public void setPillarId(String pillarId) {
        this.pillarId = pillarId;
    }

    public String getPillarName() {
        return pillarName;
    }

    public void setPillarName(String pillarName) {
        this.pillarName = pillarName;
    }
}
