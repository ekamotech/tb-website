package com.example.website.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "group_members", uniqueConstraints = @UniqueConstraint(columnNames = {"groupId", "userId"}))
@Data
@EqualsAndHashCode(callSuper=false)
public class GroupMember extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Authority {
        ROLE_USER, ROLE_ADMIN
    };
    
    @Id
    @SequenceGenerator(name = "group_member_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long groupId;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;
    
    @ManyToOne
    @JoinColumn(name = "groupId", insertable = false, updatable = false)
    private Group group;
    
    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;
    
}
