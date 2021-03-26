package com.isd.internship.service;

import com.isd.internship.entity.User;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.UserResponse;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    ResponseEntity<Object> createUser(User user);

    List<User> readAllUsers();

    User readUserById(Long userId);

    ResponseEntity<Object> updateUser(User user,Long id);

    void deleteUser(long userId);

    boolean checkEmailAvailability(String email);

    boolean checkUsernameAvailability(String username);

    User getOne(Long userId);

    User save(User user);

    ApiResponse followCategory(Long userId,Long categoryId);

    ApiResponse joinGroup(Long userId,Long groupId);

    ApiResponse leaveGroup(Long groupId,Long userId);

    User findById(Long id);

    UserResponse getUserResponse(Long userId);

    boolean isVerified(String verificationCode);

    void sendVerificationEmail(User user) throws UnsupportedEncodingException, MessagingException;

    ApiResponse updateImage(Long userId, Long currentUserId,String imageUrl);

    String getUserProfileImage(Long currentUserId,Long userId);

}
