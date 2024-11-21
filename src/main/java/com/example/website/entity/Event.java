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
import lombok.ToString;

@Entity
@Table(name = "events")
@Data
@EqualsAndHashCode(callSuper=false)
@ToString(onlyExplicitlyIncluded = true)
public class Event extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "event_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    
    @Column(nullable = false, length = 100)
    private String title;
    
    @Column(nullable = false, length = 1000)
    private String description;
    
//    @Column(name = "start_date", nullable = false)
//    private LocalDateTime startDate;
//    
//    @Column(name = "end_date", nullable = false)
//    private LocalDateTime endDate;
//    
//    @Column(nullable = false, length = 100)
//    private String location;
//    
//    @Column(nullable = false, precision = 9, scale = 6)
//    private BigDecimal latitude;
//
//    @Column(nullable = false, precision = 9, scale = 6)
//    private BigDecimal longitude;
//    
//    @Column(nullable = false)
//    private String path;
    
    

}
