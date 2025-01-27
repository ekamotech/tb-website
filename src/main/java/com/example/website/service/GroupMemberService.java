package com.example.website.service;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.website.entity.Group;
import com.example.website.entity.GroupMember;
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
    
    /**
     * グループに参加者を登録します。
     *
     * @param userId ユーザーID
     * @param groupId グループID
     */
    @Transactional
    public void joinGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));
        
        // 既に参加済みかを確認
        boolean isMember = isUserGroupMember(userId, groupId);
        if (isMember) {
            throw new IllegalStateException("既にこのグループに参加しています");
        }
        
        // グループ参加者として登録
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMember.setAuthority(GroupMember.Authority.ROLE_USER);
        groupMemberRepository.save(groupMember);
        
    }
    
    /**
     * ユーザーをグループから脱退させます。
     *
     * @param userId ユーザーID
     * @param groupId グループID
     */
    @Transactional
    public void leaveGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("指定されたグループは見つかりませんでした"));
        
        // GroupMemberエンティティを検索
        GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new IllegalArgumentException("グループ参加情報が見つかりません"));

        // ユーザーがグループ管理者である場合、例外をスロー
        if (groupMember.getAuthority() == GroupMember.Authority.ROLE_ADMIN) {
            throw new IllegalArgumentException("グループ管理者はグループを脱退できません");
        }
        
        // エンティティを削除
        groupMemberRepository.delete(groupMember);
    }

}
