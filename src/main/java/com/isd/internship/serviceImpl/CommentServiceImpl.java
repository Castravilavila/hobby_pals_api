package com.isd.internship.serviceImpl;

import com.isd.internship.entity.*;
import com.isd.internship.exceptions.ResourceNotFoundException;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.CommentResponse;
import com.isd.internship.payload.PagedResponse;
import com.isd.internship.repository.CommentRepository;
import com.isd.internship.repository.PostRepository;
import com.isd.internship.repository.UserRepository;
import com.isd.internship.service.CommentService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    @Override
    public ApiResponse createComment(Comment comment, Long postId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",postId));

        Comment savedComment = new Comment(comment.getText(), Calendar.getInstance().getTime());
        savedComment.setPostId(post);
        savedComment.setUserId(user);
        commentRepository.save(savedComment);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{commentId}")
                .build(post.getPostId());

        try {
            sendCommentNotification(post.getUserId(),user,post);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ApiResponse(true,"Comment created successfully!");
    }

    @Override
    public ApiResponse deleteComment(Long commentId){

        Comment comment = commentRepository.getOne(commentId);
        commentRepository.delete(comment);

        return new ApiResponse(true,"Comment deleted successfully!");
    }


    @Override
    public PagedResponse<CommentResponse> getCommentsPaginated(Long postId, PageRequest pageRequest,Long currentUserId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","Id",postId));
        Page<Comment> commentPage = commentRepository.findByPostId(post, pageRequest);
        List<CommentResponse> commentResponses = commentPage.getContent().stream().map(c->getCommentResponse(c,currentUserId)).collect(Collectors.toList());
        PagedResponse<CommentResponse> pagedResponse = new PagedResponse<CommentResponse>(commentResponses,commentPage.getNumber(),commentPage.getSize(),commentPage.getTotalElements(),commentPage.getTotalPages(),commentPage.isLast());

        return pagedResponse;
    }

    private CommentResponse getCommentResponse(Comment comment,Long currentUserId){

        User user = comment.getUserId();
        boolean createdByCurrentUser = currentUserId.equals(comment.getUserId().getId());
        return new CommentResponse(comment.getText(),user.getUsername(),user.getId(),comment.getDate(),comment.getId(),createdByCurrentUser,user.getImageUrl());
    }

    @Override
    public ApiResponse updateComment(Long commentId,Comment commentBody){
        Comment comment = commentRepository.getOne(commentId);
        comment.setText(commentBody.getText());
        comment.setDate(new Date());
        commentRepository.save(comment);

        return new ApiResponse(true,"Comment updated successfully!");
    }

    private void sendCommentNotification(User postOwner,User commentOwner, Post post)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = postOwner.getEmail();
        String fromAddress = "HobbyPals";
        String senderName = "HobbyPals.com";
        String subject = "You have a new comment under your post.";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to check the new comment under your post [[postTitle]] by [[commentOwner]]<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Check Comments</a></h3>"
                + "Thank you,<br>"
                + "HobbyPals.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", postOwner.getName());
        content = content.replace("[[postTitle]]",post.getPostTitle());
        content = content.replace("[[commentOwner]]",commentOwner.getUsername());
        String siteURL = "https://hobby-pals2front.herokuapp.com/";
        String postURL = siteURL + "blogPost/" +post.getPostId();
        content = content.replace("[[URL]]", postURL);
        helper.setText(content, true);
        mailSender.send(message);

    }
}
