package com.isd.internship.payload;

public class JoinRequestResponse {

    private Long id;

    private String candidateUsername;

    private Long candidateId;

    private String message;

    private String groupName;

    private Long groupId;

    public JoinRequestResponse(Long id,String candidateUsername, Long candidateId, String message, String groupName, Long groupId) {
        this.candidateUsername = candidateUsername;
        this.candidateId = candidateId;
        this.message = message;
        this.groupName = groupName;
        this.groupId = groupId;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCandidateUsername() {
        return candidateUsername;
    }

    public void setCandidateUsername(String candidateUsername) {
        this.candidateUsername = candidateUsername;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
