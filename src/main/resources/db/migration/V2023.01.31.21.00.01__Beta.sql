create table Beta (
    id              bigint          not null,
    epoch           int             not null    default 0,
    active_status   varchar(24)     not null,
    created_ts      bigint          not null
);

alter table Beta add constraint "beta__pkey" primary key (id, created_ts);

create unique index if not exists "beta_active__index" on Beta (id, active_status, created_ts);