create table Gamma_to_Alpha (
    gamma_id         bigint          not null,
    alpha_id         bigint          not null,
    weight           bigint          not null     default 0,
    is_active        bool            not null,
    is_active_gamma  varchar(32)     not null,
    is_active_alpha  varchar(32)     not null,
    can_use          bool            not null,
    created_ts       bigint          not null
);

alter table Gamma_to_Alpha add constraint "gamma_to_alpha__pkey" primary key (gamma_id, alpha_id, created_ts);

create index if not exists "ga_gamma_can_use__index" on Gamma_to_Alpha (gamma_id, can_use, created_ts);

create index if not exists "ga_alpha_can_use__index" on Gamma_to_Alpha (alpha_id, can_use, created_ts);


create or replace function fill_ga_can_use ()
    returns trigger language plpgsql as $$
begin
    if new.is_active and new.is_active_gamma = 'TRUE' and new.is_active_alpha = 'TRUE' then
        new.can_use := true;
    else
        new.can_use := false;
    end if;
    return new;
end $$;

create trigger ab_can_use__trigger
    before insert or update on Gamma_to_Alpha
    for each row execute procedure fill_ga_can_use();