package com.isd.internship.payload;

public class PostRequest {

    private String imageUrl;

    private String text;

    private String title;

    public PostRequest(String imageUrl, String text, String title) {
        this.imageUrl = imageUrl;
        this.text = text;
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
