package com.isd.internship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_group")
@Getter
@Setter
@NoArgsConstructor
public class Group {
    // id title description icon category access
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_title", nullable = false)
    private String groupTitle;

    @Column(name = "group_description", nullable = false)
    private String groupDescription;

    @ManyToMany
    @JoinTable(
            name = "group_user",
            joinColumns = {@JoinColumn(name = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    @JsonIgnore
    Set<User> users = new HashSet<>();

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    private Category categoryId;

    @Column(name = "group_access", nullable = false)
    private String groupAccess;

    private String imageUrl;

    @OneToMany(mappedBy = "requestedGroup",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<JoinRequest> joinRequests;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<UserGroup> userGroups;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "groupId")
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    @Override
    public String toString() {
        return "Group{" +
                "group_id=" + groupId +
                ", groupTitle='" + groupTitle + '\'' +
                ", groupDescription='" + groupDescription + '\'' +
                ", categoryId=" + categoryId +
                ", groupAccess='" + groupAccess + '\'' +
                '}';
    }

    public Group(String groupTitle, String groupDescription,String imageUrl,String groupAccess) {
        this.groupTitle = groupTitle;
        this.groupDescription = groupDescription;
        this.imageUrl = imageUrl;
        this.groupAccess = groupAccess;
    }
}
