package com.isd.internship.repository;


import com.isd.internship.entity.Comment;
import com.isd.internship.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

     Page<Comment> findByPostId(Post id, Pageable pageable);
}