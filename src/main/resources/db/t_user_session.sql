CREATE TABLE t_user_session (
                                id BIGSERIAL PRIMARY KEY,
                                public_id UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                                internal_id VARCHAR(10) NOT NULL UNIQUE,
                                user_internal_id VARCHAR(10) NOT NULL,
                                session_id VARCHAR(255) NOT NULL,
                                is_active BOOLEAN NOT NULL,
                                last_activity TIMESTAMP,
                                reserved_field_1 VARCHAR(255),
                                reserved_field_2 VARCHAR(255),
                                reserved_field_3 VARCHAR(255),
                                reserved_field_4 TIMESTAMP,
                                reserved_field_5 TIMESTAMP,
                                reserved_field_6 VARCHAR(255),
                                reserved_field_7 VARCHAR(255),
                                reserved_field_8 VARCHAR(255),
                                reserved_field_9 VARCHAR(255),
                                reserved_field_10 TEXT,
                                created_by VARCHAR(255),
                                created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                update_by VARCHAR(255),
                                update_date TIMESTAMP,
                                FOREIGN KEY (user_internal_id) REFERENCES m_user (internal_id)
);
