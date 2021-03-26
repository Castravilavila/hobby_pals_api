package com.isd.internship.payload;

public class CategoryResponse {

    private String name;

    private Long id;

    private boolean followedByCurrentUser;

    private String imageUrl;

    public CategoryResponse(String name, Long id, boolean followedByCurrentUser, String imageUrl) {
        this.name = name;
        this.id = id;
        this.followedByCurrentUser = followedByCurrentUser;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFollowedByCurrentUser() {
        return followedByCurrentUser;
    }

    public void setFollowedByCurrentUser(boolean followedByCurrentUser) {
        this.followedByCurrentUser = followedByCurrentUser;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
