package com.example.website.form;

import com.example.website.entity.GroupMember.Authority;

import lombok.Data;

@Data
public class GroupMemberForm {
    
    private Long id;
    
    private Long groupId;
    
    private Long userId;
    
    private Authority authority;

}
