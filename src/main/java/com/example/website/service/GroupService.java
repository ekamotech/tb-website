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

import com.example.website.entity.Group;
import com.example.website.entity.GroupMember;
import com.example.website.entity.User;
import com.example.website.entity.UserInf;
import com.example.website.form.GroupForm;
import com.example.website.form.UserForm;
import com.example.website.repository.GroupMemberRepository;
import com.example.website.repository.GroupRepository;
import com.example.website.repository.UserRepository;

@Service
public class GroupService {
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private UserRepository userRepository;
    
    public List<GroupForm> getGroupsForAdmin(Principal principal) throws IOException {
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();

        List<GroupMember> groupMembers = groupMemberRepository.findByUserIdAndAuthority(userInf.getUserId(), GroupMember.Authority.ROLE_ADMIN);
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
    
    public GroupForm getGroup(UserInf user, Group entity) throws IOException {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(Group.class, GroupForm.class).addMappings(mapper -> mapper.skip(GroupForm::setUser));

        GroupForm form = modelMapper.map(entity, GroupForm.class);

        UserForm userForm = modelMapper.map(entity.getUser(), UserForm.class);
        form.setUser(userForm);

        return form;
    }
    
    @Transactional
    public void createGroup(Principal principal, GroupForm form) throws IOException {
        Group entity = new Group();
        Authentication authentication = (Authentication) principal;
        UserInf userInf = (UserInf) authentication.getPrincipal();

        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        entity.setCreatedBy(userInf.getUserId());
        groupRepository.saveAndFlush(entity);

        // UserInf から User オブジェクトを取得
        User user = userRepository.findById(userInf.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));

        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(entity.getId());
        groupMember.setUserId(user.getUserId());
        groupMember.setAuthority(GroupMember.Authority.ROLE_ADMIN);
        groupMember.setGroup(entity);
        groupMember.setUser(user);
        groupMemberRepository.saveAndFlush(groupMember);
    }
    
    @Transactional
    public void updateGroup(Principal principal, GroupForm form) throws IOException {
        Group entity = groupRepository.findById(form.getId()).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        entity.setName(form.getName());
        entity.setDescription(form.getDescription());
        groupRepository.saveAndFlush(entity);
    }

}
