create table Alpha (
    id          bigint          not null    primary key,
    property_1  varchar(100)    not null    default 'property 1',
    property_2  bigint          not null    default 0,
    property_3  bigint          not null    default 0
);