package com.isd.internship.repository;

import com.isd.internship.entity.Group;
import com.isd.internship.entity.JoinRequest;
import com.isd.internship.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JoinRequestRepository extends JpaRepository<JoinRequest,Long> {

    Page<JoinRequest> getByRequestedGroup(Group group, Pageable pageable);

    Optional<JoinRequest> findByRequestedGroupAndCandidate(Group group, User user);

    Optional<JoinRequest> findById(Long id);
}
