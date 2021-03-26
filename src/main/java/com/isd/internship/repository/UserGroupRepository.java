package com.isd.internship.repository;

import com.isd.internship.entity.Group;
import com.isd.internship.entity.User;
import com.isd.internship.entity.UserGroup;
import com.isd.internship.entity.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {

    Optional<UserGroup> findByUserIdAndGroupGroupId(Long userId, Long groupId);


    UserGroup findUserGroupByUserGroupRoleAndGroup(UserGroupRole role, Group group);

}
