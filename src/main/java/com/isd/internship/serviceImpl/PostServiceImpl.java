package com.isd.internship.serviceImpl;

import com.isd.internship.entity.Category;
import com.isd.internship.entity.Group;
import com.isd.internship.entity.Post;
import com.isd.internship.entity.User;
import com.isd.internship.exceptions.ResourceNotFoundException;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.payload.PostRequest;
import com.isd.internship.payload.PostResponse;
import com.isd.internship.repository.GroupRepository;
import com.isd.internship.repository.PostRepository;
import com.isd.internship.repository.UserRepository;
import com.isd.internship.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {


    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    @Override
    public ApiResponse updatePost(Long postId, PostRequest postRequest){

        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",postId));
        post.setPostText(postRequest.getText());
        post.setImageUrl(postRequest.getImageUrl());
        post.setPostTitle(postRequest.getTitle());

        postRepository.save(post);
        return new ApiResponse(true,"Post updated successfully");
    }

    @Override
    public ResponseEntity<Post> createPost(PostRequest postRequest, Long groupId, Long userId){

        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        Group group = groupRepository.findByGroupId(groupId).orElseThrow(()->new ResourceNotFoundException("Group","Id",groupId));

        Post post = new Post(postRequest.getTitle(), postRequest.getText(), postRequest.getImageUrl());
        post.setGroupId(group);
        post.setUserId(user);
        postRepository.save(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{postId}")
                .build(post.getPostId());

        return ResponseEntity.created(location).build();
    }

    private PostResponse getPostResponse(Post post,Long currentUserId){

        boolean accessToEdit = post.getUserId().getId()==currentUserId;
        User user = post.getUserId();
        Group group = post.getGroupId();
        Category category = group.getCategoryId();
        return new PostResponse(post.getPostId(),post.getPostTitle(), post.getPostText(),
                post.getImageUrl(),accessToEdit,post.getUserId().getUsername(),post.getPostDate(),
                group.getGroupId(),group.getGroupTitle(),category.getCategoryName(),category.getCategoryId(),user.getImageUrl());
    }

    @Override
    public PostResponse findById(Long id,Long currentUserId){

        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",id));
        return getPostResponse(post,currentUserId);
    }

    private PagedResponse<PostResponse> getPagedResponse(Page<Post> page,Long currentUserId){


        List<PostResponse> postResponses = page.getContent().stream().map(p->getPostResponse(p,currentUserId)).collect(Collectors.toList());
        return PagedResponse.getPagedResponse(postResponses,page);
    }

    @Override
    public PagedResponse<PostResponse> getByGroupId(Long groupId,PageRequest pageRequest,Long currentUserId){
        Group group = groupRepository.findByGroupId(groupId).orElseThrow(()->new ResourceNotFoundException("Group","Id",groupId));
        Page<Post> postPage = postRepository.findByGroupId(group,pageRequest);
        return getPagedResponse(postPage,currentUserId);
    }

    @Override
    public Page<Post> getPostsByTitle(String title, PageRequest pageRequest) {
        return postRepository.findPostsByPostTitle(title, pageRequest);
    }

    @Override
    public void deletePostById(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",postId));
        postRepository.delete(post);
    }

    @Override
    public PagedResponse<PostResponse> getPostsFromFollowedCategoriesByUserId(Long userId,int page,Long currentUserId){

        Pageable pageable = PageRequest.of(page,6);
        Page<Post> postPage = postRepository.getPostsFromFollowedCategoriesByUserId(userId,pageable);

        return getPagedResponse(postPage,currentUserId);
    }

    @Override
    public PagedResponse<PostResponse> getPostsFromJoinedGroupsByUserId(Long userId,int page,Long currentUserId){
        Pageable pageable = PageRequest.of(page,6);
        Page<Post> postPage = postRepository.getPostsFromJoinedGroupsByUserId(userId,pageable);

        return getPagedResponse(postPage,currentUserId);
    }
}
