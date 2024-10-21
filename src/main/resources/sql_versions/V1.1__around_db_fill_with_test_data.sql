INSERT INTO public.teams(color)
VALUES('BLUE');

INSERT INTO public.teams(color)
VALUES('YELLOW');

INSERT INTO public.teams(color)
VALUES('PURPLE');

INSERT INTO public.teams(color)
VALUES('DARK_PURPLE');

INSERT INTO public.rounds(starts, ends,active)
Values('2024-09-11 00:00:00'::TIMESTAMP , '2024-12-14 23:59:59'::TIMESTAMP, true );

INSERT INTO public.cities(chunks)
Values('[
{"id":"8610c221fffffff"},
{"id":"8610c22f7ffffff"},
{"id":"8610c22afffffff"},
{"id":"8610c22d7ffffff"},
{"id":"8610c228fffffff"},
{"id":"8610c2287ffffff"},
{"id":"8610c22b7ffffff"},
{"id":"8610dc92fffffff"},
{"id":"8610dc927ffffff"},
{"id":"8610c229fffffff"},
{"id":"8610c2297ffffff"},
{"id":"8610dc907ffffff"},
{"id":"8610dc937ffffff"},
{"id":"8610c266fffffff"},
{"id":"8610dc917ffffff"},
{"id":"8610c264fffffff"}
]' );

INSERT INTO public.users ("level", coins, username, avatar, "password", "role", email, verified)
VALUES(1, 0, 'username','guest.jpg', '$2a$10$yatZBPQE4uvwPoRSwN/8ZuyJXpBY8HTOGlknkfB4dTbfNlREA4UsS',
       'USER', 'mikefixeloqq@gmail.com', true);

INSERT INTO public.costs
(id, cost_value)
VALUES(0, '[
  0,
  10,
  20,
  40,
  80,
  160,
  320,
  680,
  1360,
  2720,
  5440,
  10880
]');

INSERT INTO public.rules
(id, rule_value)
VALUES(0, '[
  1,
  2,
  3,
  4,
  5,
  6,
  7,
  8,
  9,
  10,
  11,
  12
]');

INSERT INTO public.users_rounds_team_city(user_id, round_id, team_id, city_id)
VALUES (1,1,1, 1);

INSERT INTO public.skills
("name", max_level, description, image_name, icon, rule_id, cost_id)
VALUES('width', 11, ''::character varying, ''::character varying, '1'::character varying, 0, 0);

INSERT INTO public.users_skills
(user_id, skill_id, current_level)
VALUES(1, 1, 0);

INSERT INTO public.event_providers
(id, name, url)
VALUES (1,'timepad','dev.timepad.ru');

INSERT INTO chunks(id, round_id,city_id, owner)
values (1,1,1,1);

INSERT INTO map_events(id,provider_id,"name",starts_at,ends_at,url,verified,ad)
values (1,1,'Тестовое событие','2024-09-11 00:00:00'::TIMESTAMP,'2024-12-14 23:59:59'::TIMESTAMP,'',false, false);

INSERT INTO map_events_chunks(event_id, round_id, city_id, chunk_id)
VALUES (1,1,1,1);
