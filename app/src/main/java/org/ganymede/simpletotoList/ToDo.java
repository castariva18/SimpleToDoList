package org.ganymede.simpletotoList;

public class ToDo {
    private String id,title,description,timeline, status;


    public ToDo(String id, String title, String description, String timeline, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.timeline = timeline;
        this.status = status;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTimeline() {
        return timeline;
    }

    public void setTimeline(String timeline) {
        this.timeline = timeline;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

}