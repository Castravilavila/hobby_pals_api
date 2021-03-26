package com.isd.internship.service;

import com.isd.internship.entity.Group;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.GroupRequest;
import com.isd.internship.payload.GroupResponse;
import com.isd.internship.payload.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface GroupService {
    ResponseEntity<Object> createGroup(GroupRequest group, Long categoryId,Long userId);//C

    List<Group> readAllGroups();//R

    GroupResponse readGroupById(Long groupId, Long userId);//R

    ResponseEntity<Object> updateGroup(Group group,Long id);//U

    ApiResponse deleteGroup(Long groupId);//D

    PagedResponse<GroupResponse> readAllByCategoryId(Long categoryId,int page,Long currentUserId);

    Page<Group> getGroupsByTitle(String title, PageRequest pageRequest);

    Page<Group> getGroupsByFollowedCategories(String title, PageRequest pageRequest, Long userId);

    Group findById(Long groupId);

    Group save(Group group);

    PagedResponse<GroupResponse> getGroupsFollowedByUser(Long userId,int page);

    List<GroupResponse> getGroupsByUserId(Long userId);
}
