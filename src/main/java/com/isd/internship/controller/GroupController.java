package com.isd.internship.controller;

import com.isd.internship.entity.Category;
import com.isd.internship.entity.Group;
import com.isd.internship.payload.*;
import com.isd.internship.security.CurrentUser;
import com.isd.internship.security.UserPrincipal;
import com.isd.internship.service.GroupService;
import com.isd.internship.service.JoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final JoinRequestService joinRequestService;

    @PostMapping("/groups/{categoryId}")
    public ResponseEntity<Object> createGroup(@RequestBody GroupRequest group, @PathVariable Long categoryId,@CurrentUser UserPrincipal currentUser) {
        return groupService.createGroup(group,categoryId,currentUser.getId());
    }

    @GetMapping("/groups")
    public List<Group> readAllGroups(){
        return groupService.readAllGroups();
    }

    @GetMapping("/groups/{groupId}")
    public GroupResponse readGroupById(@PathVariable Long groupId, @CurrentUser UserPrincipal currentUser){
        return groupService.readGroupById(groupId,currentUser.getId());
    }

    @GetMapping("/groups/userResponse/{userId}")
    public List<GroupResponse> readGroupById(@PathVariable Long userId){
        return groupService.getGroupsByUserId(userId);
    }

    @GetMapping("/groups/update/{groupId}")
    public ResponseEntity<Object> updateGroup(@RequestBody Group group,@PathVariable Long groupId){
        return groupService.updateGroup(group, groupId);
    }

    @GetMapping("/groups/byCategory/{categoryId}")
    public PagedResponse<GroupResponse> getGroupsByCategory(@PathVariable Long categoryId,
                                                            @CurrentUser UserPrincipal currentUser,
                                                            @RequestParam int page){
        return groupService.readAllByCategoryId(categoryId,page,currentUser.getId());
    }

    @GetMapping("/groups/search")
    @Transactional
    public Page<Group> getGroupsSearchFilteredPaginated(@RequestParam(name = "title") Optional<String> title,
                                                        @RequestParam Optional<Integer> page,
                                                        @RequestParam Optional<String> sortBy){
        return groupService.getGroupsByTitle(title.orElse(""), PageRequest.of(page.orElse(0),
                10, Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @GetMapping("/groups/searchInUserCategories")
    public Page<Group> getGroupsByFollowedCategories(@CurrentUser UserPrincipal userPrincipal,
                                                        @RequestParam(name = "title") Optional<String> title,
                                                        @RequestParam Optional<Integer> page,
                                                        @RequestParam Optional<String> sortBy){
        return groupService.getGroupsByFollowedCategories(title.orElse(""),
                PageRequest.of(page.orElse(0), 10, Sort.Direction.ASC,
                        sortBy.orElse("id")),userPrincipal.getId());
    }


    @GetMapping("/groups/followedBy")
    public PagedResponse<GroupResponse> getGroupsFollowedByUser(@CurrentUser UserPrincipal userPrincipal,
                                                                @RequestParam int page){

        return groupService.getGroupsFollowedByUser(userPrincipal.getId(),page);
    }

    @DeleteMapping("/groups/{groupId}")
    public ApiResponse deleteGroup(@PathVariable Long groupId){

        return groupService.deleteGroup(groupId);
    }

    @GetMapping("/groups/joinRequests/{groupId}")
    public PagedResponse<JoinRequestResponse> getJoinRequestsByGroupId(@PathVariable Long groupId,
                                                                       @RequestParam int page){

        return joinRequestService.getPagedJoinRequestResponse(groupId,page);
    }

    @PostMapping("/groups/joinRequests/{groupId}")
    public ApiResponse createGroupJoinRequest(@PathVariable Long groupId,
                                              @CurrentUser UserPrincipal currentUser,
                                              @RequestBody String message){

        return joinRequestService.create(groupId,currentUser.getId(),message);
    }

    @PostMapping("/groups/joinRequests/answer/{joinRequestId}/{isAccepted}")
    public ApiResponse handleJoinRequest(@PathVariable Long joinRequestId,
                                         @PathVariable int  isAccepted,
                                         @CurrentUser UserPrincipal currentUser){

        return joinRequestService.handleJoinRequest(joinRequestId,isAccepted,currentUser.getId());
    }


}
