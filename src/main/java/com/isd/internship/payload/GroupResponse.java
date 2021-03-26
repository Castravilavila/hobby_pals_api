package com.isd.internship.payload;

import com.isd.internship.entity.UserGroupRole;

import java.io.Serializable;

public class GroupResponse implements Serializable {

    private String name;

    private String description;

    private UserGroupRole currentUserRole;

    private String imageUrl;

    private Long id;

    private String accessType;

    private String categoryName;

    private Long categoryId;

    private Long creatorId;

    private boolean pendingRequest;

    public GroupResponse(String name, String description, UserGroupRole currentUserRole, String imageUrl, Long id, String accessType, String categoryName, Long categoryId,  Long creatorId,boolean pendingRequest) {
        this.name = name;
        this.description = description;
        this.currentUserRole = currentUserRole;
        this.imageUrl = imageUrl;
        this.id = id;
        this.accessType = accessType;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.creatorId = creatorId;
        this.pendingRequest = pendingRequest;
    }

    public boolean isPendingRequest() {
        return pendingRequest;
    }

    public void setPendingRequest(boolean pendingRequest) {
        this.pendingRequest = pendingRequest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserGroupRole getCurrentUserRole() {
        return currentUserRole;
    }

    public void setCurrentUserRole(UserGroupRole currentUserRole) {
        this.currentUserRole = currentUserRole;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
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

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
}
