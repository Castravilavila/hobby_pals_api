package com.isd.internship.payload;

import com.isd.internship.entity.Category;

import java.util.Date;

public class PostResponse {

    private Long id;

    private String title;

    private String text;

    private String imageUrl;

    private boolean accessToEdit;

    private String creatorUsername;

    private Date postDate;

    private Long groupId;

    private String groupName;

    private String categoryName;

    private Long categoryId;

    private String creatorImageUrl;

    public PostResponse(Long id, String title, String text, String imageUrl,
                        boolean accessToEdit, String creatorUsername, Date postDate,
                        Long groupId, String groupName, String categoryName, Long categoryId,String creatorImageUrl) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.accessToEdit = accessToEdit;
        this.creatorUsername = creatorUsername;
        this.postDate = postDate;
        this.groupId = groupId;
        this.groupName = groupName;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.creatorImageUrl = creatorImageUrl;
    }

    public String getCreatorImageUrl() {
        return creatorImageUrl;
    }

    public void setCreatorImageUrl(String creatorImageUrl) {
        this.creatorImageUrl = creatorImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAccessToEdit() {
        return accessToEdit;
    }

    public void setAccessToEdit(boolean accessToEdit) {
        this.accessToEdit = accessToEdit;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
