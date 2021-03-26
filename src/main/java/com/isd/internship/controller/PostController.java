package com.isd.internship.controller;


import com.isd.internship.entity.Post;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.payload.PostRequest;
import com.isd.internship.payload.PostResponse;
import com.isd.internship.security.CurrentUser;
import com.isd.internship.security.UserPrincipal;
import com.isd.internship.service.PostService;
import lombok.RequiredArgsConstructor;
import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/posts/{groupId}")
    public ApiResponse createPost(@PathVariable Long groupId,
                                  @RequestBody PostRequest postRequest,
                                  @CurrentUser UserPrincipal currentUser){

        postService.createPost(postRequest,groupId,currentUser.getId());
        return new ApiResponse(true,"Post created successfully");
    }

    @GetMapping("/posts/{postId}")
    public PostResponse getPostById(@PathVariable Long postId,
                                    @CurrentUser UserPrincipal currentUser){
        return postService.findById(postId,currentUser.getId());
    }

    @GetMapping("/posts/inGroup/{groupId}")
    public PagedResponse<PostResponse> getPostsByGroupId(@PathVariable Long groupId,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam int page){
        return postService.getByGroupId(groupId, PageRequest.of(page,6),currentUser.getId());
    }

    @GetMapping("/posts/search")
    @Transactional
    public Page<Post> getPostsSearchFilteredPaginated(@RequestParam Optional<String> title,
                                                      @RequestParam Optional<Integer> page,
                                                      @RequestParam Optional<String> sortBy){
        return postService.getPostsByTitle(title.orElse(""), PageRequest.of(page.orElse(0), 2, Sort.Direction.ASC, sortBy.orElse("id")));
    }

    @PutMapping("/posts/{postId}")
    public ApiResponse updatePost(@PathVariable Long postId,@RequestBody PostRequest postRequest){

        return postService.updatePost(postId, postRequest);
    }

    @DeleteMapping("/posts/{postId}")
    public ApiResponse deletePost(@PathVariable Long postId){

        postService.deletePostById(postId);
        return new ApiResponse(true,"Post deleted successfully!");
    }

    @GetMapping("/posts/followedCategories/{userId}")
    public PagedResponse<PostResponse> getPostsFromFollowedCategoriesByUserId(@PathVariable Long userId,
                                                                              @CurrentUser UserPrincipal currentUser,
                                                                              @RequestParam int page){

        return postService.getPostsFromFollowedCategoriesByUserId(userId==-1?currentUser.getId():userId,page,currentUser.getId());
    }

    @GetMapping("/posts/joinedGroups/{userId}")
    public PagedResponse<PostResponse> getPostsFromJoinedGroupsByUserId(@PathVariable Long userId,
                                                                        @CurrentUser UserPrincipal currentUser,
                                                                        @RequestParam int page){

        return postService.getPostsFromJoinedGroupsByUserId(userId==-1?currentUser.getId():userId,page,currentUser.getId());
    }
}
