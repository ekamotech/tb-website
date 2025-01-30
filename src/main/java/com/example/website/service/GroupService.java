package com.example.website.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.website.entity.Event;
import com.example.website.entity.Group;
import com.example.website.entity.GroupMember;
import com.example.website.entity.User;
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
    private final UserService userService;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    
    public GroupService(UserRepository userRepository, UserService userService, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository, EventRepository eventRepository, EventService eventService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }
    
    /**
     * 指定されたユーザーが管理者であるグループ一覧を取得します。
     *
     * @return グループフォームのリスト
     * @throws IOException 入出力例外が発生した場合
     */
    public List<GroupForm> getGroupsForAdmin() throws IOException {
        List<GroupForm> list = new ArrayList<>();
        
        Optional<Long> optionalUserId = userService.getUserId();
        optionalUserId.orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        
        optionalUserId.ifPresent(userId -> {
            List<GroupMember> groupMembers = groupMemberRepository.findByUserIdAndAuthorityOrderByUpdatedAtDesc(userId);
            List<Group> groups = groupMembers.stream()
                                             .map(GroupMember::getGroup)
                                             .collect(Collectors.toList());
            
            for (Group entity : groups) {
                try {
                    GroupForm form = getGroup(entity.getId());
                    list.add(form);
                } catch (IOException e) {
                    // 例外を適切に処理する
                    e.printStackTrace();
                }
            }
        });

        return list;
    }
    
    /**
     * 指定されたグループの情報を取得します。
     *
     * @param groupId グループID
     * @return グループフォームオブジェクト
     * @throws IOException 入出力例外が発生した場合
     */
    public GroupForm getGroup(Long groupId) throws IOException {
        
        Group entity = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("グループが見つかりません"));
        
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Group.class, GroupForm.class).addMappings(mapper -> mapper.skip(GroupForm::setCreatedBy));
        modelMapper.typeMap(Group.class, GroupForm.class).addMappings(mapper -> mapper.skip(GroupForm::setEvents));

        GroupForm form = modelMapper.map(entity, GroupForm.class);

        UserForm userForm = modelMapper.map(entity.getCreatedBy(), UserForm.class);
        form.setCreatedBy(userForm);
        
        List<EventForm> events = new ArrayList<EventForm>();

        // 更新日時が新しい順にイベントを取得
        List<Event> eventEntities = eventRepository.findByGroupIdOrderByUpdatedAtDesc(groupId);

        for (Event eventEntity : eventEntities) {
            EventForm event = eventService.getEvent(eventEntity.getId());
            events.add(event);
        }
        form.setEvents(events);
        
        return form;
    }
    
    /**
     * 新規グループを作成します。
     *
     * @param userId ユーザーID
     * @param form グループフォームオブジェクト
     * @throws IOException 入出力例外が発生した場合
     */
    @Transactional
    public void createGroup(Long userId, GroupForm form) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません"));
        
        Group entity = new Group();
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
     * @param userId ユーザーID
     * @param form グループフォームオブジェクト
     * @throws IOException 入出力例外が発生した場合
     */
    @Transactional
    public void updateGroup(Long userId, GroupForm form) throws IOException {
        Group entity = groupRepository.findById(form.getId()).orElseThrow(() -> new IllegalArgumentException("グループが見つかりません"));
        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        groupRepository.saveAndFlush(entity);
    }

}
