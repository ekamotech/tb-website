package com.example.website.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Data;

/**
 * エンティティの共通のプロパティとメソッドを提供する抽象クラス。
 * 作成日時と更新日時のフィールドを持ち、
 * エンティティの永続化および更新の前にこれらのフィールドを自動的に設定します。
 */
@MappedSuperclass
@Data
public class AbstractEntity {
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    public void onPrePersist() {
        Date date = new Date();
        setCreatedAt(date);
        setUpdatedAt(date);
    }

    @PreUpdate
    public void onPreUpdate() {
        setUpdatedAt(new Date());
    }
}
