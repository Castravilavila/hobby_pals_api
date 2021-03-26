package com.isd.internship.repository;

import com.isd.internship.entity.Group;
import com.isd.internship.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findById(Long id);

    Page<Post> findByGroupId(Group group,Pageable pageable);

    @Query("SELECT p from Post p WHERE p.postTitle LIKE %?1%")
    Page<Post> findPostsByPostTitle(String title, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.groupId.groupId IN(SELECT g.groupId FROM Group g WHERE g.categoryId.categoryId IN(SELECT c.categoryId FROM Category c JOIN c.users u WHERE u.id = :userId)) ORDER BY p.postDate")
    Page<Post> getPostsFromFollowedCategoriesByUserId(Long userId,Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.groupId.groupId IN(SELECT g.groupId FROM Group g JOIN g.users u WHERE u.id = :userId) ORDER BY p.postDate")
    Page<Post> getPostsFromJoinedGroupsByUserId(Long userId,Pageable pageable);
}
