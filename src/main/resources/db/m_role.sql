CREATE TABLE m_role (
                        id BIGSERIAL PRIMARY KEY,
                        public_id UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
                        internal_id VARCHAR(10) NOT NULL UNIQUE,
                        role VARCHAR(255) NOT NULL,
                        "group" VARCHAR(255) NOT NULL,
                        reserved_field_1 VARCHAR(255),
                        reserved_field_2 VARCHAR(255),
                        reserved_field_3 VARCHAR(255),
                        reserved_field_4 TIMESTAMP,
                        reserved_field_5 TEXT,
                        created_by VARCHAR(255),
                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        update_by VARCHAR(255),
                        update_date TIMESTAMP
);
