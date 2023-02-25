create table Alpha_to_Beta (
    alpha_id        bigint          not null,
    beta_id         bigint          not null,
    property_1      varchar(100)    not null      default 'property_1'
);

alter table Alpha_to_Beta add constraint "alpha_to_beta__pkey" primary key (alpha_id, beta_id);

alter table Alpha_to_Beta add constraint "alpha_to_beta__fkey__Alpha_id" foreign key (alpha_id) references Alpha (id);

alter table Alpha_to_Beta add constraint "alpha_to_beta__fkey__Beta_id" foreign key (beta_id) references Beta (id);