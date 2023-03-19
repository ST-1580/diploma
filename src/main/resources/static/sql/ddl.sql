insert into alpha (id, name, active_status, created_ts)
values
    (1, 'hello', 'TRUE', 0),
    (2, 'goodbye', 'TRUE', 1),
    (3, 'gg', 'TRUE', 1),
    (4, 'test', 'TRUE', 2),
    (5, 'prod', 'TRUE', 3),
    (6, 'alone(', 'TRUE', 8);

insert into beta (id, epoch, active_status, created_ts)
values
    (83, 0, 'TRUE', 1),
    (85, 12, 'TRUE', 4),
    (88, 1, 'TRUE', 5),
    (90, 33, 'TRUE', 2),
    (91, -64, 'TRUE', 8);

insert into gamma (id, is_master, active_status, created_ts)
values
    (70, true, 'TRUE', 0),
    (72, false, 'TRUE', 8),
    (77, false, 'TRUE', 5),
    (80, true, 'TRUE', 4),
    (83, true, 'TRUE', 3);

insert into delta (id, name, active_status, created_ts)
values
    (10, 'solo', 'TRUE', 7),
    (11, 'top', 'TRUE', 2),
    (12, 'hi', 'TRUE', 1);

insert into alpha_to_beta (alpha_id, beta_id, hash, is_active, is_active_alpha, is_active_beta, created_ts)
values
    (1, 85, '1_85', true, 'TRUE', 'TRUE', 5),
    (2, 88, '2_88', true, 'TRUE', 'TRUE', 4),
    (3, 90, '3_90', true, 'TRUE', 'TRUE', 2),
    (4, 83, '4_83', true, 'TRUE', 'TRUE', 1),
    (5, 83, '5_83', true, 'TRUE', 'TRUE', 0),
    (5, 85, '5_85', true, 'TRUE', 'TRUE', 0),
    (6, 91, '6_91', true, 'TRUE', 'TRUE', 9);

insert into gamma_to_alpha (gamma_id, alpha_id, weight, is_active, is_active_gamma, is_active_alpha, created_ts)
values
    (70, 1, 100, true, 'TRUE', 'TRUE', 1),
    (70, 2, 228, true, 'TRUE', 'TRUE', 5),
    (77, 2, 0, true, 'TRUE', 'TRUE', 4),
    (83, 3, -1, true, 'TRUE', 'TRUE', 2),
    (80, 4, 111, true, 'TRUE', 'TRUE', 2),
    (80, 5, 1431, true, 'TRUE', 'TRUE', 3),
    (72, 6, 651, true, 'TRUE', 'TRUE', 9);

insert into gamma_to_delta (gamma_id, delta_id, is_active, is_active_gamma, is_active_delta, created_ts)
values
    (72, 10, true, 'TRUE', 'TRUE', 10),
    (83, 11, true, 'TRUE', 'TRUE', 1),
    (80, 11, true, 'TRUE', 'TRUE', 2),
    (77, 12, true, 'TRUE', 'TRUE', 3),
    (70, 12, true, 'TRUE', 'TRUE', 5);

insert into last_sync (name, last_sync_ts)
values
    ('ALPHA', 0),
    ('BETA', 0),
    ('GAMMA', 0),
    ('DELTA', 0),
    ('LINK_CORRECTOR', 0)