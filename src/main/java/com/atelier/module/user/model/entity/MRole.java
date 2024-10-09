package com.atelier.module.user.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_role")
public class MRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(name = "public_id", updatable = false, nullable = false, unique = true)
    private UUID publicId;

    @Column(name = "internal_id", unique = true)
    private String internalId;

    @Column(name = "role")
    private String role;

    @Column(name = "\"group\"")
    private String group;

    @Column(name = "reserved_field_1")
    private String reservedField1;

    @Column(name = "reserved_field_2")
    private String reservedField2;

    @Column(name = "reserved_field_3")
    private String reservedField3;

    @Column(name = "reserved_field_4")
    private LocalDateTime reservedField4;

    @Column(name = "reserved_field_5", columnDefinition = "TEXT")
    private String reservedField5;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MUser> users;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MPermission> permissions;

}

