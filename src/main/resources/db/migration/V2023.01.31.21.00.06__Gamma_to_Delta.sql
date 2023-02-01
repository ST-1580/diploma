create table Gamma_to_Delta (
        Gamma_id        bigint      not null,
        Delta_id        bigint      not null
);

alter table Gamma_to_Delta add constraint "gamma_to_delta__pkey" primary key (Gamma_id, Delta_id);

alter table Gamma_to_Delta add constraint "gamma_to_delta__fkey__Gamma_id" foreign key (Gamma_id) references Gamma (id);

alter table Gamma_to_Delta add constraint "gamma_to_delta__fkey__Delta_id" foreign key (Delta_id) references Delta (id);