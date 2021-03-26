package com.isd.internship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "post_title")
    private String postTitle;

    private String imageUrl;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreationTimestamp
    private Date postDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group groupId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User userId;

    @OneToMany(mappedBy = "postId",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Comment> commentSet = new HashSet<>();

    public Post(String title,String text,String imageUrl){
        this.postTitle = title;
        this.postText = text;
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postText='" + postText + '\'' +
                ", postDate=" + postDate +
                ", groupId=" + groupId +
                ", userId=" + userId +
                '}';
    }
}
