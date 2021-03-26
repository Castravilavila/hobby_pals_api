package com.isd.internship.serviceImpl;

import com.isd.internship.entity.*;
import com.isd.internship.exceptions.ResourceNotFoundException;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.UserResponse;
import com.isd.internship.repository.CategoryRepository;
import com.isd.internship.repository.UserGroupRepository;
import com.isd.internship.repository.UserRepository;
import com.isd.internship.service.GroupService;
import com.isd.internship.service.UserService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final GroupService groupService;

    private final CategoryRepository categoryRepository;

    private final UserGroupRepository userGroupRepository;

    private final JavaMailSender mailSender;

    @Override
    public ResponseEntity<Object> createUser(User user) {
        User saveUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/userId")
                .build(saveUser.getId());
        return ResponseEntity.created(location).build();
    }

    @Override
    public List<User> readAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User readUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            System.out.println("There is no user with such id " + userId);
            //return null;
        }
        return user.get();
    }

    @Override
    public ResponseEntity<Object> updateUser(User user, Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        user.setId(id);
        userRepository.save(user);

        return ResponseEntity.noContent().build();

    }

    @Override
    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean checkEmailAvailability(String email) {
        Optional<User> user = userRepository.getUserByEmail(email);
        return !user.isPresent();
    }

    @Override
    public boolean checkUsernameAvailability(String username) {
        Optional<User> user = userRepository.getUserByUsername(username);
        return !user.isPresent();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "Id", id));
    }

    @Override
    public UserResponse getUserResponse(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        return new UserResponse(user.getName(), user.getSurname());
    }

    @Override
    public User getOne(Long userId) {
        return userRepository.getOne(userId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public ApiResponse followCategory(Long userId, Long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));
        User user = userRepository.getOne(userId);
        user.getCategories().add(category);
        userRepository.save(user);

        return new ApiResponse(true, "Category followed successfully");
    }

    @Override
    public ApiResponse joinGroup(Long userId, Long groupId) {

        Group group = groupService.findById(groupId);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        UserGroup userGroup = new UserGroup();
        userGroup.setGroup(group);
        userGroup.setUser(user);
        userGroup.setEnrolmentDate(new Date());
        userGroup.setUserGroupRole(UserGroupRole.ROLE_MEMBER);
        group.getUsers().add(user);
        userGroupRepository.save(userGroup);

        return new ApiResponse(true, "User joined the group successfully");

    }

    @Override
    public ApiResponse leaveGroup(Long groupId, Long userId) {

        UserGroup userGroup = userGroupRepository.findByUserIdAndGroupGroupId(userId, groupId).orElseThrow(() -> new ResourceNotFoundException("UserGroup", "Id", groupId));
        userGroupRepository.delete(userGroup);

        return new ApiResponse(true, "User left the group successfully");
    }

    @Override
    public boolean isVerified(String verificationCode) {
        User user = userRepository.findByCodeVerification(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setCodeVerification(null);
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
    }

    public void sendVerificationEmail(User user) throws UnsupportedEncodingException, MessagingException {
        String toAddress = user.getEmail();
        String fromAddress = "HobbyPals.com";
        String senderName = "HobbyPals";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "HobbyPals.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getName());
        String siteURL = "https://hobby-pals-back2.herokuapp.com/api/users";
        String verifyURL = siteURL + "/verify?code=" + user.getCodeVerification();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public ApiResponse updateImage(Long userId,Long currentUserId,String imageUrl){

        User user;

        if(userId==-1){
            user = findById(currentUserId);
        }else{
            user = findById(userId);
        }
        user.setImageUrl(imageUrl);
        userRepository.save(user);

        return new ApiResponse(true,"Image updated successfully");
    }

    @Override
    public String getUserProfileImage(Long currentUserId,Long userId){
        User user;
        if(userId==-1){
            user = findById(currentUserId);
        }else user = findById(userId);

        return user.getImageUrl();
    }
}