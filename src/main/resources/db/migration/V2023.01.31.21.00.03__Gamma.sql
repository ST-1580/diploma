create table Gamma (
    id              bigint          not null,
    is_master       bool            not null    default false,
    active_status   varchar(24)     not null,
    created_ts      bigint          not null
);

alter table Gamma add constraint "gamma__pkey" primary key (id, created_ts);

create unique index if not exists "gamma_active__index" on Gamma (id, created_ts, active_status);