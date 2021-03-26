package com.isd.internship.controller;

import com.isd.internship.entity.*;
import com.isd.internship.payload.*;
import com.isd.internship.repository.UserGroupRepository;
import com.isd.internship.security.CurrentUser;
import com.isd.internship.security.UserPrincipal;
import com.isd.internship.service.CategoryService;
import com.isd.internship.service.GroupService;
import com.isd.internship.service.UserService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private final CategoryService categoryService;

    private final GroupService groupService;

    private final UserGroupRepository userGroupRepository;

    @GetMapping("/users")
    public List<User> getUsers(){
        return userService.readAllUsers();
    }

    @GetMapping("/users/{userId}")
    public User readUserById(@PathVariable Long userId,@CurrentUser UserPrincipal currentUser){
        return userService.readUserById(userId==-1?currentUser.getId():userId);
    }

    @PutMapping("/users/update/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody User user,@PathVariable Long userId) {
        return userService.updateUser(user, userId);
    }

    @GetMapping("/users/userResponse/{userId}")
    public UserResponse getUserResponse(@PathVariable Long userId){
        return userService.getUserResponse(userId);
    }

    @PostMapping("/users/checkEmail")
    public boolean checkEmailAvailability(@RequestParam String email){
        return userService.checkEmailAvailability(email);
    }

    @PostMapping("/users/checkUsername")
    public boolean checkUsernameAvailability(@RequestParam String username){
        return userService.checkUsernameAvailability(username);
    }

    @PostMapping("/users/followCategory/{categoryId}")
    public ApiResponse followCategory(@CurrentUser UserPrincipal userPrincipal,@PathVariable Long categoryId){

        return userService.followCategory(userPrincipal.getId(),categoryId);
    }

    @PostMapping("/users/leaveGroup/{groupId}")
    public ApiResponse leaveGroup(@PathVariable Long groupId,@CurrentUser UserPrincipal currentUser){

        return userService.leaveGroup(groupId,currentUser.getId());
    }

    @PostMapping("/users/joinGroup/{groupId}")
    public ApiResponse joinGroup(@CurrentUser UserPrincipal currentUser,@PathVariable Long groupId){

        return userService.joinGroup(currentUser.getId(),groupId);
    }


    @GetMapping("/users/verify")
    public void verifyUser(@Param("code") String code, HttpServletResponse response) throws IOException {
        if (userService.isVerified(code)) response.sendRedirect("https://hobby-pals2front.herokuapp.com/login");
        else response.sendRedirect("https://hobby-pals2front.herokuapp.com/signin");
    }
    @PutMapping("/users/image/{userId}")
    public ApiResponse updateProfileImage(@PathVariable Long userId,
                                          @RequestBody String imageUrl,
                                          @CurrentUser UserPrincipal currentUser){
        return userService.updateImage(userId,currentUser.getId(),imageUrl);
    }

    @GetMapping("/users/image/{userId}")
    public Map<String,String> getUserProfileImage(@CurrentUser UserPrincipal currentUser, @PathVariable Long userId){

        Map<String,String> map = new HashMap<>();
        map.put("imageUrl",userService.getUserProfileImage(currentUser.getId(),userId));
        return map;
    }
}
