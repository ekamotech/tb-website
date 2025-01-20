package com.example.website.service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.website.entity.Event;
import com.example.website.entity.Group;
import com.example.website.entity.GroupMember;
import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.EventForm;
import com.example.website.form.GroupForm;
import com.example.website.form.UserForm;
import com.example.website.repository.EventRepository;
import com.example.website.repository.GroupMemberRepository;
import com.example.website.repository.GroupRepository;
import com.example.website.repository.UserRepository;

/**
 * グループに関連するサービスクラス。
 * グループの作成、更新、取得などの操作を提供します。
 */
@Service
public class GroupService {
    
    @Autowired
    private ModelMapper modelMapper;
    
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    
    public GroupService(UserRepository userRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, EventRepository eventRepository, EventService eventService) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }
    
    /**
     * 指定されたユーザーが管理者であるグループ一覧を取得します。
     *
     * @param principal 認証されたユーザー情報
     * @return グループフォームのリスト
     * @throws IOException 入出力例外が発生した場合
     */
    public List<GroupForm> getGroupsForAdmin(Principal principal) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();
        
        // UserInf から User オブジェクトを取得
        User user = userRepository.findById(userInf.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<GroupMember> groupMembers = groupMemberRepository.findByUserAndAuthorityOrderByUpdatedAtDesc(user, GroupMember.Authority.ROLE_ADMIN);
        List<Group> groups = groupMembers.stream()
                                         .map(GroupMember::getGroup)
                                         .collect(Collectors.toList());

        List<GroupForm> list = new ArrayList<>();
        for (Group entity : groups) {
            GroupForm form = getGroup(userInf, entity);
            list.add(form);
        }
        return list;
    }
    
    /**
     * 指定されたグループの情報を取得します。
     *
     * @param user 認証されたユーザー情報
     * @param entity グループエンティティ
     * @return グループフォームオブジェクト
     * @throws IOException 入出力例外が発生した場合
     */
    public GroupForm getGroup(UserInf user, Group entity) throws IOException {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Group.class, GroupForm.class).addMappings(mapper -> mapper.skip(GroupForm::setCreatedBy));
        modelMapper.typeMap(Group.class, GroupForm.class).addMappings(mapper -> mapper.skip(GroupForm::setEvents));

        GroupForm form = modelMapper.map(entity, GroupForm.class);

        UserForm userForm = modelMapper.map(entity.getCreatedBy(), UserForm.class);
        form.setCreatedBy(userForm);
        
        List<EventForm> events = new ArrayList<EventForm>();

        // 更新日時が新しい順にイベントを取得
        List<Event> eventEntities = eventRepository.findByGroupOrderByUpdatedAtDesc(entity);

        for (Event eventEntity : eventEntities) {
            EventForm event = eventService.getEvent(user, eventEntity);
            events.add(event);
        }
        form.setEvents(events);
        
        return form;
    }
    
    /**
     * 新規グループを作成します。
     *
     * @param principal 認証されたユーザー情報
     * @param form グループフォームオブジェクト
     * @throws IOException 入出力例外が発生した場合
     */
    @Transactional
    public void createGroup(Principal principal, GroupForm form) throws IOException {
        Group entity = new Group();
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();
        
        // UserInf から User オブジェクトを取得
        User user = userRepository.findById(userInf.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        entity.setCreatedBy(user);
        groupRepository.saveAndFlush(entity);

        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(entity);
        groupMember.setUser(user);
        groupMember.setAuthority(GroupMember.Authority.ROLE_ADMIN);
        groupMemberRepository.saveAndFlush(groupMember);
    }
    
    /**
     * グループを更新します。
     *
     * @param principal 認証されたユーザー情報
     * @param form グループフォームオブジェクト
     * @throws IOException 入出力例外が発生した場合
     */
    @Transactional
    public void updateGroup(Principal principal, GroupForm form) throws IOException {
        Group entity = groupRepository.findById(form.getId()).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        groupRepository.saveAndFlush(entity);
    }

}
