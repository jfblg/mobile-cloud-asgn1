package org.magnum.dataup.model;

public class MyVideo {

    private long id;
    private String title;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MyVideo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
