create table Beta (
    id          bigint          not null    primary key,
    name        varchar(100)    not null    default 'beta',
    property_1  bigint          not null    default 0,
    property_2  bool            not null    default true
);