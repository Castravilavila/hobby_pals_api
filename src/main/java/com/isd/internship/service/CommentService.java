package com.isd.internship.service;

import com.isd.internship.entity.Comment;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.CommentResponse;
import com.isd.internship.payload.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface CommentService {

     ApiResponse createComment(Comment comment, Long postId, Long userId);

     ApiResponse deleteComment(Long commentId);

     ApiResponse updateComment(Long commentId,Comment comment);

     PagedResponse<CommentResponse> getCommentsPaginated(Long postId,PageRequest pageRequest,Long currentUserId);

}

