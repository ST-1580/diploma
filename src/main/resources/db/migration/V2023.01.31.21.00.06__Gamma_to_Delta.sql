create table Gamma_to_Delta (
        gamma_id         bigint          not null,
        delta_id         bigint          not null,
        is_active        bool            not null,
        is_active_gamma  varchar(32)     not null,
        is_active_delta  varchar(32)     not null,
        can_use          bool            not null,
        created_ts       bigint          not null
);

alter table Gamma_to_Delta add constraint "gamma_to_delta__pkey" primary key (gamma_id, delta_id, created_ts);

create index if not exists "gd_gamma_can_use__index" on Gamma_to_Delta (gamma_id, can_use, created_ts);

create index if not exists "gd_delta_can_use__index" on Gamma_to_Delta (delta_id, can_use, created_ts);


create or replace function fill_gd_can_use ()
    returns trigger language plpgsql as $$
begin
    if new.is_active and new.is_active_gamma = 'TRUE' and new.is_active_delta = 'TRUE' then
        new.can_use := true;
    else
        new.can_use := false;
    end if;
    return new;
end $$;

create trigger ab_can_use__trigger
    before insert or update on Gamma_to_Delta
    for each row execute procedure fill_gd_can_use();