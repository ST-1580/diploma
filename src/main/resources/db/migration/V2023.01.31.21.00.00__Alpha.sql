create table Alpha (
    id          bigint          not null,
    name        varchar(100)    not null,
    is_active   varchar(24)     not null,
    created_ts  bigint          not null
);

alter table Alpha add constraint "alpha__pkey" primary key (id, created_ts);

create unique index if not exists "alpha_active__index" on Alpha (id, is_active, created_ts);
