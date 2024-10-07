package com.atelier.module.auth.model.entity;

import com.atelier.module.auth.model.AuthenticationType;
import com.atelier.module.user.model.entity.MUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user_auth")
public class TUserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(name = "public_id", updatable = false, nullable = false, unique = true)
    private UUID publicId;

    @Column(name = "internal_id", unique = true)
    private String internalId;

    @ManyToOne
    @JoinColumn(name = "user_internal_id", referencedColumnName = "internal_id")
    private MUser user;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type")
    private AuthenticationType authenticationType;

    @Column(name = "password")
    private String password;

    @Column(name = "term_condition")
    private Boolean termCondition;

    @Column(name = "biometric_template")
    private String biometricTemplate;

    @Column(name = "reserved_field_1")
    private String reservedField1;

    @Column(name = "reserved_field_2")
    private String reservedField2;

    @Column(name = "reserved_field_3")
    private String reservedField3;

    @Column(name = "reserved_field_4")
    private LocalDateTime reservedField4;

    @Column(name = "reserved_field_5")
    private LocalDateTime reservedField5;

    @Column(name = "reserved_field_6")
    private String reservedField6;

    @Column(name = "reserved_field_7")
    private String reservedField7;

    @Column(name = "reserved_field_8")
    private String reservedField8;

    @Column(name = "reserved_field_9")
    private String reservedField9;

    @Column(name = "reserved_field_10", columnDefinition = "TEXT")
    private String reservedField10;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

}

