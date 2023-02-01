create table Gamma_to_Alpha (
       Gamma_id        bigint      not null,
       Alpha_id        bigint      not null
);

alter table Gamma_to_Alpha add constraint "gamma_to_alpha__pkey" primary key (Gamma_id, Alpha_id);

alter table Gamma_to_Alpha add constraint "gamma_to_alpha__fkey__Gamma_id" foreign key (Gamma_id) references Gamma (id);

alter table Gamma_to_Alpha add constraint "gamma_to_alpha__fkey__Alpha_id" foreign key (Alpha_id) references Alpha (id);