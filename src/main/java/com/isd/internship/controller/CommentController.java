package com.isd.internship.controller;


import com.isd.internship.entity.Comment;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.CommentResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.security.CurrentUser;
import com.isd.internship.security.UserPrincipal;
import com.isd.internship.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{postId}")
    public ApiResponse createComment(@RequestBody Comment comment,
                                     @PathVariable Long postId,
                                     @CurrentUser UserPrincipal currentUser){
        return commentService.createComment(comment, postId, currentUser.getId());
    }

    @GetMapping("/comments/paginated/{postId}")
    @Transactional
    public PagedResponse<CommentResponse> getCommentsPaginated(@PathVariable Long postId,
                                                               @RequestParam Optional<Integer> page,
                                                               @RequestParam Optional<String> sortBy,
                                                               @CurrentUser UserPrincipal currentUser) {
        return commentService.getCommentsPaginated(postId, PageRequest.of(page.orElse(0), 5, Sort.Direction.ASC, sortBy.orElse("id")),currentUser.getId());
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }

    @PutMapping("/comments/update/{commentId}")
    public ApiResponse updateComment(@PathVariable Long commentId,@RequestBody Comment comment){
        return commentService.updateComment(commentId,comment);
    }
}
