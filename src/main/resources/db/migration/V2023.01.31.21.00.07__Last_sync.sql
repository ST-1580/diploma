create table Last_sync (
    name            varchar(100)    not null,
    last_sync_ts    bigint          not null
);

create index if not exists "last_sync_name__index" on Last_sync (name);