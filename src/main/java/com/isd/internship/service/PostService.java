package com.isd.internship.service;


import com.isd.internship.entity.Post;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.payload.PostRequest;
import com.isd.internship.payload.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PostService {

    ResponseEntity<Post> createPost(PostRequest post, Long groupId, Long userId);

    PostResponse findById(Long id,Long currentUserId);

    PagedResponse<PostResponse> getByGroupId(Long groupId, PageRequest pageRequest,Long currentUserId);

    Page<Post> getPostsByTitle(String title, PageRequest pageRequest);

    ApiResponse updatePost(Long postId, PostRequest postRequest);

    void deletePostById(Long postId);

    PagedResponse<PostResponse> getPostsFromFollowedCategoriesByUserId(Long userId,int page,Long currentUserId);

    PagedResponse<PostResponse> getPostsFromJoinedGroupsByUserId(Long userId,int page,Long currentUserId);

}
