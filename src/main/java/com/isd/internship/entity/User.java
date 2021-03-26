package com.isd.internship.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    @Column(name = "verification_code", length = 64)
    private String codeVerification;
    private boolean enabled;


    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "candidate",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<JoinRequest> joinRequests;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    @JoinTable(name="user_role", joinColumns = {
        @JoinColumn(name="user_id", nullable=false,updatable=false)},inverseJoinColumns = {
        @JoinColumn(name="role_id", nullable=false, updatable = false)})
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    @JsonIgnoreProperties("users")
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "category_user",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    @JsonIgnoreProperties("users")
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserGroup> userGroups;
}
