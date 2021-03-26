package com.isd.internship.serviceImpl;

import com.isd.internship.entity.*;
import com.isd.internship.exceptions.ResourceNotFoundException;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.GroupRequest;
import com.isd.internship.payload.GroupResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.repository.*;
import com.isd.internship.service.GroupService;
import com.isd.internship.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final UserGroupRepository userGroupRepository;

    private final JoinRequestRepository joinRequestRepository;

    @Override
    public ResponseEntity<Object> createGroup(GroupRequest groupRequest, Long categoryId,Long creatorId) {

        Group group = new Group(groupRequest.getTitle(), groupRequest.getDescription(), groupRequest.getImageUrl(), groupRequest.getGroupAccess());
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Id",categoryId));
        group.setCategoryId(category);
        User user = userRepository.findById(creatorId).orElseThrow(()->new ResourceNotFoundException("User","Id",creatorId));
        group.getUsers().add(user);
        UserGroup userGroup = new UserGroup();
        userGroup.setUser(user);
        userGroup.setGroup(group);
        userGroup.setEnrolmentDate(new Date());
        userGroup.setUserGroupRole(UserGroupRole.ROLE_ADMIN);
        save(group);
        userGroupRepository.save(userGroup);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{groupId}")
                .build(group.getGroupId());

        return ResponseEntity.created(location).build();
    }

    @Override
    public List<Group> readAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public ResponseEntity<Object> updateGroup(Group group, Long id) {
        Optional<Group> taskOptional = groupRepository.findByGroupId(id);

        group.setGroupId(id);
        groupRepository.save(group);

        return ResponseEntity.noContent().build();
    }

    private PagedResponse<GroupResponse> getPagedGroupResponse(Page<Group> groupsPage,Long currentUserId){

        List<GroupResponse> groups = groupsPage.getContent().stream()
                .map(g->getGroupResponse(g.getGroupId(),currentUserId)).collect(Collectors.toList());
        return PagedResponse.getPagedResponse(groups,groupsPage);
    }

    public List<GroupResponse> getGroupsByUserId(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
       return user.getGroups().stream().map(
                (Group g)-> {
                    Optional<JoinRequest> joinRequest = joinRequestRepository.findByRequestedGroupAndCandidate(g,user);
                    boolean pendingJoinRequest = joinRequest.isPresent();
                    Optional<UserGroup> userGroup = userGroupRepository.findByUserIdAndGroupGroupId(userId,g.getGroupId());
                    UserGroupRole currentUserRoleOnGroup = userGroup.isPresent()?userGroup.get().getUserGroupRole():UserGroupRole.ROLE_GUEST;
                    return new GroupResponse(g.getGroupTitle(), g.getGroupDescription(), currentUserRoleOnGroup, g.getImageUrl(), g.getGroupId(), g.getGroupAccess(),
                            g.getCategoryId().getCategoryName(), g.getCategoryId().getCategoryId(), userId,pendingJoinRequest);
                }).collect(Collectors.toList());
    }


    private GroupResponse getGroupResponse(Long groupId,Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        Group group = groupRepository.findByGroupId(groupId).orElseThrow(()->new ResourceNotFoundException("Group","Id",groupId));
        Optional<UserGroup> userGroup = userGroupRepository.findByUserIdAndGroupGroupId(userId,groupId);
        Category category = group.getCategoryId();
        UserGroupRole currentUserRoleOnGroup = userGroup.isPresent()?userGroup.get().getUserGroupRole():UserGroupRole.ROLE_GUEST;
        Optional<JoinRequest> joinRequest = joinRequestRepository.findByRequestedGroupAndCandidate(group,user);
        boolean pendingJoinRequest = joinRequest.isPresent();
        return new GroupResponse(group.getGroupTitle(),group.getGroupDescription(),currentUserRoleOnGroup, group.getImageUrl(),groupId,group.getGroupAccess(),category.getCategoryName(),category.getCategoryId(),userId,pendingJoinRequest);
    }

    @Override
    public GroupResponse readGroupById(Long groupId,Long userId){

        return getGroupResponse(groupId,userId);
    }

    @Override
    public PagedResponse<GroupResponse> readAllByCategoryId(Long categoryId,int page,Long currentUserId){

        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Id",categoryId));
        PageRequest pageRequest = PageRequest.of(page,6);
        Page<Group> groups = groupRepository.findByCategoryId(category,pageRequest);
        return getPagedGroupResponse(groups,currentUserId);
    }

    @Override
    public Page<Group> getGroupsByTitle(String title, PageRequest pageRequest) {
        return groupRepository.findGroupsByGroupTitleContainingIgnoreCase(title.toLowerCase(Locale.ROOT), pageRequest);
    }

    @Override
    public Page<Group> getGroupsByFollowedCategories(String title, PageRequest pageRequest, Long userId){
        List<Group> fetchedGroups= groupRepository.findGroupsByGroupTitleContainingIgnoreCase(title.toLowerCase(Locale.ROOT));
        List<Long> userCategoryIds = getAllCategoryIdsFromUser(userRepository.getOne(userId));

        List<Group> filteredGroups = fetchedGroups.stream().filter(group ->isNumberPresentInList
                .test(group.getCategoryId().getCategoryId(),userCategoryIds)).collect(Collectors.toList());

        return convertGroupListToPage(filteredGroups,pageRequest);
    }

    @Override
    public Group findById(Long groupId){
        return  groupRepository.findByGroupId(groupId).orElseThrow(()->new ResourceNotFoundException("Group","Id",groupId));
    }

    @Override
    public Group save(Group group){
        return groupRepository.save(group);
    }

    @Override
    public PagedResponse<GroupResponse> getGroupsFollowedByUser(Long userId,int page){

        PageRequest pageRequest = PageRequest.of(page,6);
        Page<Group> groupPage = groupRepository.findByUsers_Id(userId,pageRequest);

        return  getPagedGroupResponse(groupPage,userId);
    }

    @Override
    public ApiResponse deleteGroup(Long groupId){

        Group group = findById(groupId);
        groupRepository.delete(group);

        return new ApiResponse(true,"Group deleted successfully");
    }

    public List<Long> getAllCategoryIdsFromUser(User user){
        return user.getCategories().stream()
                .map(Category::getCategoryId).collect(Collectors.toList());
    }

    public Page<Group> convertGroupListToPage(List list, PageRequest pageRequest){
        int totalElements = list.size();
        int start = toIntExact(pageRequest.getOffset());
        int end = Math.min((start + pageRequest.getPageSize()), totalElements);

        List<Group> pageList =  new ArrayList<>();
        if (start <= end) {
            pageList = list.subList(start, end);
        }

        return new PageImpl<>(pageList, pageRequest, totalElements);
    }

    BiPredicate<Long, List<Long>> isNumberPresentInList = (x, y) -> y.stream().anyMatch(t -> t.equals(x));

}
