package com.isd.internship.service;

import com.isd.internship.entity.Group;
import com.isd.internship.payload.ApiResponse;
import com.isd.internship.payload.JoinRequestResponse;
import com.isd.internship.payload.PagedResponse;

public interface JoinRequestService {

    PagedResponse<JoinRequestResponse> getPagedJoinRequestResponse(Long groupId, int page);

    ApiResponse create(Long groupId,Long userId,String message);

    ApiResponse handleJoinRequest(Long joinRequestId,int isAccepted,Long currentUserId);
}
