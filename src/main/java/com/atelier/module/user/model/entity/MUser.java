package com.atelier.module.user.model.entity;

import com.atelier.module.auth.model.entity.TUserAuth;
import com.atelier.module.auth.model.entity.TUserSession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_user")
public class MUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @UuidGenerator
    @Column(name = "public_id", updatable = false, nullable = false, unique = true)
    private UUID publicId;

    @Column(name = "internal_id", unique = true)
    private String internalId;

    @ManyToOne
    @JoinColumn(name = "role_internal_id", referencedColumnName = "internal_id")
    private MRole role;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "id_card_number", unique = true)
    private String idCardNumber;

    @Column(name = "birth_date")
    private LocalDate birthDate;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TUserSession> userSessions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TUserAuth> userAuths;

    // Getters and Setters
}

