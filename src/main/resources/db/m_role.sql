create table m_role
(
    id               bigint generated by default as identity
        primary key,
    created_by       varchar(255),
    created_date     timestamp(6),
    "group"          varchar(255),
    internal_id      varchar(255)
        constraint ukqdx7vx9fmvr0qawt9y44f58nd
            unique,
    public_id        uuid not null
        constraint ukcww0q4qjrk9io1tglkxknw1ad
            unique,
    reserved_field_1 varchar(255),
    reserved_field_2 varchar(255),
    reserved_field_3 varchar(255),
    reserved_field_4 timestamp(6),
    reserved_field_5 text,
    role             varchar(255),
    update_by        varchar(255),
    update_date      timestamp(6)
);

alter table m_role
    owner to postgres;
