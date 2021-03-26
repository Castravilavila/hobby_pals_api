package com.isd.internship.payload;

public class GroupRequest {

    private String title;

    private String description;

    private String imageUrl;

    private String groupAccess;

    public GroupRequest(String title, String description, String imageUrl, String groupAccess) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.groupAccess = groupAccess;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGroupAccess() {
        return groupAccess;
    }

    public void setGroupAccess(String groupAccess) {
        this.groupAccess = groupAccess;
    }
}
