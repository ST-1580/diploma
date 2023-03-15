create table Alpha_to_Beta (
    alpha_id         bigint          not null,
    beta_id          bigint          not null,
    hash             varchar(100)    not null    default 'hash',
    is_active        bool            not null,
    is_active_alpha  varchar(32)     not null    default 'UNDEFINED',
    is_active_beta   varchar(32)     not null    default 'UNDEFINED',
    can_use          bool            not null,
    created_ts       bigint          not null
);

alter table Alpha_to_Beta add constraint "alpha_to_beta__pkey" primary key (alpha_id, beta_id, created_ts);

create index if not exists "ab_alpha_can_use__index" on Alpha_to_Beta (alpha_id, created_ts, can_use);

create index if not exists "ab_beta_can_use__index" on Alpha_to_Beta (beta_id, created_ts, can_use);

create index if not exists "ab_alpha_is_active__index" on Alpha_to_Beta (created_ts, is_active_alpha);

create index if not exists "ab_beta_is_active__index" on Alpha_to_Beta (created_ts, is_active_beta);

create or replace function fill_ab_can_use ()
    returns trigger language plpgsql as $$
begin
    if new.is_active and new.is_active_alpha = 'TRUE' and new.is_active_beta = 'TRUE' then
        new.can_use := true;
    else
        new.can_use := false;
    end if;
    return new;
end $$;

create trigger ab_can_use__trigger
    before insert or update on Alpha_to_Beta
    for each row execute procedure fill_ab_can_use();