package com.isd.internship.repository;

import com.isd.internship.entity.Category;
import com.isd.internship.entity.Group;
import com.isd.internship.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByGroupId(Long id);

    Page<Group> findByCategoryId(Category category,Pageable pageable);


    Page<Group> findGroupsByGroupTitleContainingIgnoreCase(String s,  Pageable pageable);


    List<Group> findGroupsByGroupTitleContainingIgnoreCase(String s);

    Page<Group> findByUsers_Id(Long userId,Pageable pageable);

}
