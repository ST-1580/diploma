insert into alpha (id, property_1, property_2, property_3)
values
    (1, 'hello', 1, 2),
    (2, 'goodbye', 134, 32),
    (3, 'gg', 11, 222),
    (4, 'test', -1, -1),
    (5, 'prod', 123, -766),
    (6, 'alone(', 654, 54);

insert into beta (id, property_1, property_2)
values
    (83, 0, true),
    (85, 12, true),
    (88, 1, false),
    (90, 33, true),
    (91, -64, false);

insert into gamma (id, property_1, property_2, property_3)
values
    (70, 0, 'gamma', true),
    (72, 1344, 'alone(', false),
    (77, 213, 'fake', false),
    (80, 1, 'abcdefg', true),
    (83, -563, 'negative', true);

insert into delta (id, property_1)
values
    (10, 'solo'),
    (11, 'top'),
    (12, 'hi');

insert into alpha_to_beta (alpha_id, beta_id, property_1)
values
    (1, 85, '1_85'),
    (2, 88, '2_88'),
    (3, 90, '3_90'),
    (4, 83, '4_83'),
    (5, 83, '5_83'),
    (5, 85, '5_85'),
    (6, 91, '6_91');

insert into gamma_to_alpha (gamma_id, alpha_id, property_1, property_2)
values
    (70, 1, 100, '70_1'),
    (70, 2, 228, '70_2'),
    (77, 2, 0, '77_2'),
    (83, 3, -1, '83_3'),
    (80, 4, 111, '80_4'),
    (80, 5, 1431, '80_5'),
    (72, 6, 651, '72_6');

insert into gamma_to_delta (gamma_id, delta_id)
values
    (72, 10),
    (83, 11),
    (80, 11),
    (77, 12),
    (70, 12);