package com.example.website.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "groups")
@Data
@EqualsAndHashCode(callSuper=false)
public class Group extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "group_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 1000)
    private String description;
    
    @Column(nullable = false)
    private Long createdBy;
    
    @ManyToOne
    @JoinColumn(name = "createdBy", insertable = false, updatable = false)
    private User user;
    
    
}
