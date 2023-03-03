create table Gamma (
      id          bigint          not null    primary key,
      property_1  bigint          not null    default 0,
      property_2  varchar(100)    not null    default 'property 2',
      property_3  bool            not null    default true
);