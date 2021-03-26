package com.isd.internship.payload;

import java.util.Date;

public class CommentResponse {

    private String text;

    private String creatorUsername;

    private String creatorImageUrl;

    private Long creatorId;

    private Date creationDate;

    private Long id;

    private boolean createdByCurrentUser;

    public CommentResponse(String text, String creatorUsername, Long creatorId, Date creationDate, Long id,boolean createdByCurrentUser,String creatorImageUrl) {
        this.text = text;
        this.creatorUsername = creatorUsername;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.id = id;
        this.createdByCurrentUser = createdByCurrentUser;
        this.creatorImageUrl = creatorImageUrl;
    }

    public String getCreatorImageUrl() {
        return creatorImageUrl;
    }

    public void setCreatorImageUrl(String creatorImageUrl) {
        this.creatorImageUrl = creatorImageUrl;
    }

    public boolean isCreatedByCurrentUser() {
        return createdByCurrentUser;
    }

    public void setCreatedByCurrentUser(boolean createdByCurrentUser) {
        this.createdByCurrentUser = createdByCurrentUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
