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
import lombok.ToString;

/**
 * グループのメンバーを管理するクラス。
 */
@Entity
@Table(name = "group_members", uniqueConstraints = @UniqueConstraint(columnNames = {"group_id", "user_id"}))
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(onlyExplicitlyIncluded = true)
public class GroupMember extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Authority {
        ROLE_USER, ROLE_ADMIN
    };
    
    @Id
    @SequenceGenerator(name = "group_member_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;
    

    
}
