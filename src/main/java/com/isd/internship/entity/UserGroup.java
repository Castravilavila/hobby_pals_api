package com.isd.internship.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="USERS_GROUPS")
@NoArgsConstructor
@Getter
@Setter
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_GROUP_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    @Enumerated(EnumType.STRING)
    private UserGroupRole userGroupRole;

    @Temporal(TemporalType.DATE)
    @Column(name = "ENROLMENT_DATE")
    private Date enrolmentDate;
}
