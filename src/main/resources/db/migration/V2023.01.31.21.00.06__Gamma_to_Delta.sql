create table Gamma_to_Delta (
        gamma_id        bigint      not null,
        delta_id        bigint      not null
);

alter table Gamma_to_Delta add constraint "gamma_to_delta__pkey" primary key (gamma_id, delta_id);

alter table Gamma_to_Delta add constraint "gamma_to_delta__fkey__Gamma_id" foreign key (gamma_id) references Gamma (id);

alter table Gamma_to_Delta add constraint "gamma_to_delta__fkey__Delta_id" foreign key (delta_id) references Delta (id);