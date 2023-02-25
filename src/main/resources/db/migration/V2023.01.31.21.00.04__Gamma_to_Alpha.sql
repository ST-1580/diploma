create table Gamma_to_Alpha (
       gamma_id        bigint          not null,
       alpha_id        bigint          not null,
       property_1      bigint          not null      default 0,
       property_2      varchar(100)    not null      default 'property_2'
);

alter table Gamma_to_Alpha add constraint "gamma_to_alpha__pkey" primary key (gamma_id, alpha_id);

alter table Gamma_to_Alpha add constraint "gamma_to_alpha__fkey__Gamma_id" foreign key (gamma_id) references Gamma (id);

alter table Gamma_to_Alpha add constraint "gamma_to_alpha__fkey__Alpha_id" foreign key (alpha_id) references Alpha (id);