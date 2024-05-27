INSERT INTO public.team (color)
VALUES('BLUE');

INSERT INTO public.team (color)
VALUES('YELLOW');

INSERT INTO public.team (color)
VALUES('PURPLE');

INSERT INTO public.team (color)
VALUES('DARK_PURPLE');

INSERT INTO public.game_user ("level", coins, username, team_id, city, "password", "role", email, verified)
VALUES(1, 0, 'username', 1, 'Yekaterinburg', '$2a$10$yatZBPQE4uvwPoRSwN/8ZuyJXpBY8HTOGlknkfB4dTbfNlREA4UsS',
       'USER', 'mikefixeloqq@gmail.com', true);

INSERT INTO public."cost"
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

INSERT INTO public."rule"
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

INSERT INTO public.skill
("name", max_level, description, image_name, rule_id, cost_id)
VALUES('width', 11, ''::character varying, ''::character varying, 0, 0);

INSERT INTO public.user_skills
(user_id, skill_id, current_level)
VALUES(1, 1, 0);

INSERT INTO public.event_providers
(id, name, url)
VALUES (0,'timepad','dev.timepad.ru')