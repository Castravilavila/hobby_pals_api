package com.isd.internship.serviceImpl;

import com.isd.internship.entity.*;
import com.isd.internship.exceptions.ResourceNotFoundException;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.JoinRequestResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.repository.JoinRequestRepository;
import com.isd.internship.repository.UserGroupRepository;
import com.isd.internship.repository.UserRepository;
import com.isd.internship.service.GroupService;
import com.isd.internship.service.JoinRequestService;
import com.isd.internship.service.UserService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JoinRequestServiceImpl implements JoinRequestService {


    private final JoinRequestRepository joinRequestRepository;

    private final GroupService groupService;

    private final UserService userService;

    private final UserGroupRepository userGroupRepository;

    private final JavaMailSender mailSender;

    private final UserRepository userRepository;

    private JoinRequestResponse getJoinRequestResponse(JoinRequest joinRequest) {

        User candidate = joinRequest.getCandidate();
        Group group = joinRequest.getRequestedGroup();
        return new JoinRequestResponse(joinRequest.getId(), candidate.getUsername()
                , candidate.getId(), joinRequest.getMessage()
                , group.getGroupTitle(), group.getGroupId());

    }

    @Override
    public PagedResponse<JoinRequestResponse> getPagedJoinRequestResponse(Long groupId, int page) {

        Group group = groupService.findById(groupId);
        Pageable pageRequest = PageRequest.of(page, 6);
        Page<JoinRequest> joinRequestPage = joinRequestRepository.getByRequestedGroup(group, pageRequest);
        List<JoinRequestResponse> joinRequestResponseList = joinRequestPage.getContent().stream()
                .map(this::getJoinRequestResponse).collect(Collectors.toList());

        return PagedResponse.getPagedResponse(joinRequestResponseList, joinRequestPage);
    }

    @Override
    public ApiResponse create(Long groupId, Long userId, String message) {


        User user = userService.getOne(userId);
        Group group = groupService.findById(groupId);
        UserGroup adminId = userGroupRepository.findUserGroupByUserGroupRoleAndGroup(UserGroupRole.ROLE_ADMIN, group);
        User groupAdmin = userService.findById(adminId.getUser().getId());

        Optional<JoinRequest> joinRequestOptional = joinRequestRepository.findByRequestedGroupAndCandidate(group, user);
        if (joinRequestOptional.isPresent()) return new ApiResponse(false, "There is already an pending join request");


        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setCandidate(user);
        joinRequest.setRequestedGroup(group);
        joinRequest.setMessage(message);
        joinRequestRepository.save(joinRequest);
        try {
            sendJoinRequestNotification(groupAdmin, user, group);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ApiResponse(true, "Request created successfully!");
    }

    @Override
    public ApiResponse handleJoinRequest(Long joinRequestId, int isAccepted, Long currentUserId) {

        ApiResponse apiResponse;


        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("JoinRequest", "Id", joinRequestId));
        User user = joinRequest.getCandidate();
        Group group = joinRequest.getRequestedGroup();
        UserGroup ug = userGroupRepository.findByUserIdAndGroupGroupId(currentUserId, group.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("User Group", "Id", currentUserId));


        UserGroup adminId = userGroupRepository.findUserGroupByUserGroupRoleAndGroup(UserGroupRole.ROLE_ADMIN, group);
        User groupAdmin = userService.findById(adminId.getUser().getId());

        if(isAccepted>0&&ug.getUserGroupRole()==UserGroupRole.ROLE_ADMIN){
            group.getUsers().add(user);
            UserGroup userGroup = new UserGroup();
            userGroup.setUser(user);
            userGroup.setGroup(group);
            userGroup.setEnrolmentDate(new Date());
            userGroup.setUserGroupRole(UserGroupRole.ROLE_MEMBER);
            groupService.save(group);
            userGroupRepository.save(userGroup);

            apiResponse = new ApiResponse(true, "User accepted by the admin of the group");
            try {
                sendJoinRequestResponse(groupAdmin,user,group,"accepted");
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            apiResponse = new ApiResponse(false, "User declined by the admin of the group");
            try {
                sendJoinRequestResponse(groupAdmin,user,group,"declined");
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        joinRequestRepository.delete(joinRequest);
        return apiResponse;
    }

    private void sendJoinRequestNotification(User groupOwner, User requestSender, Group group)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = groupOwner.getEmail();
        String fromAddress = "HobbyPals";
        String senderName = "HobbyPals.com";
        String subject = "You have a new request to join your private group.";
        String content = "Dear [[name]],<br>"
                + "Please  check the new request to join your private group <b>[[groupName]]</b> by <b>[[requestSender]]</b><br>"
                + "Thank you,<br>"
                + "HobbyPals.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", groupOwner.getName());
        content = content.replace("[[groupName]]", group.getGroupTitle());
        content = content.replace("[[requestSender]]", requestSender.getUsername());
        helper.setText(content, true);
        mailSender.send(message);

    }

    private void sendJoinRequestResponse(User groupOwner, User requestSender, Group group,String joinRequestResponse)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = requestSender.getEmail();
        String fromAddress = "HobbyPals";
        String senderName = "HobbyPals.com";
        String subject = "Join request answer.";
        String content = "Dear [[name]],<br>"
                + "Your request to join the private group <b>[[groupName]]</b> "
                + "has been <b>[[response]]</b> by the group admin <b>[[adminUsername]]</b><br>"
                + "Thank you,<br>"
                + "HobbyPals.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[adminUsername]]", groupOwner.getName());
        content = content.replace("[[groupName]]", group.getGroupTitle());
        content = content.replace("[[name]]", requestSender.getUsername());
        content = content.replace("[[response]]",joinRequestResponse);
        helper.setText(content, true);
        mailSender.send(message);
    }
}