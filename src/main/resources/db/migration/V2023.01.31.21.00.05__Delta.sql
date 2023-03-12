create table Delta (
    id              bigint          not null,
    name            varchar(100)    not null,
    active_status   varchar(24)     not null,
    created_ts      bigint          not null
);

alter table Delta add constraint "delta__pkey" primary key (id, created_ts);

create unique index if not exists "delta_active__index" on Delta (id, active_status, created_ts);