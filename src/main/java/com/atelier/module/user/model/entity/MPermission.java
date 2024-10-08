package com.atelier.module.user.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "m_permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(name = "public_id", updatable = false, nullable = false, unique = true)
    private UUID publicId;

    @Column(name = "internal_id", unique = true)
    private String internalId;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "name")
    private String name;

    @Column(name = "notes")
    private String notes;

    @Column(name = "type")
    private String type;

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

    @ManyToOne
    @JoinColumn(name = "role_internal_id", referencedColumnName = "internal_id")
    private MRole role;
}
