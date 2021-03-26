package com.isd.internship.repository;

import com.isd.internship.entity.User;
import com.isd.internship.entity.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username,String email);

    @Query("SELECT u FROM User u WHERE u.codeVerification = ?1")
    User findByCodeVerification(String code);




}
