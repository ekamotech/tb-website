package com.example.website.service;

import org.springframework.stereotype.Service;

import com.example.website.entity.Group;
import com.example.website.entity.User;
import com.example.website.repository.GroupMemberRepository;
import com.example.website.repository.GroupRepository;
import com.example.website.repository.UserRepository;

/**
 * グループメンバーに関連するサービスクラス。
 * グループメンバーの管理や操作を提供します。
 */
@Service
public class GroupMemberService {
    
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberService(UserRepository userRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }
    
    /**
     * 指定されたユーザーが特定のグループの管理者であるかを判定します。
     *
     * @param userId ユーザーのID
     * @param groupId グループのID
     * @return ユーザーがグループの管理者である場合は true、それ以外の場合は false
     */
    public boolean isUserGroupAdmin(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));

        return groupMemberRepository.isUserGroupAdmin(userId, groupId);
    }
    
    /**
     * 指定されたユーザーが特定のグループに参加しているかを判定します。
     *
     * @param userId ユーザーのID
     * @param groupId グループのID
     * @return ユーザーがグループに参加している場合は true、それ以外の場合は false
     */
    public boolean isUserGroupMember(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));

        return groupMemberRepository.isUserGroupMember(userId, groupId);
    }
    
}
