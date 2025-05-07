package org.example.omnibeuser.service;

import org.example.omnibeuser.dto.MemberResDto;

public interface AdminService {

    MemberResDto.DeleteMemberForAdmin deleteMemberForAdmin(Long memberId);

}
