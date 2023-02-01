create table Alpha_to_Beta (
    Alpha_id        bigint      not null,
    Beta_id         bigint      not null
);

alter table Alpha_to_Beta add constraint "alpha_to_beta__pkey" primary key (Alpha_id, Beta_id);

alter table Alpha_to_Beta add constraint "alpha_to_beta__fkey__Alpha_id" foreign key (Alpha_id) references Alpha (id);

alter table Alpha_to_Beta add constraint "alpha_to_beta__fkey__Beta_id" foreign key (Beta_id) references Beta (id);