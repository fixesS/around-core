INSERT INTO public.teams(color)
VALUES('BLUE'),('YELLOW'),('PURPLE'),('DARK_PURPLE');

INSERT INTO settings.game(id,user_level_exp_cost_rule)
values (1,'[
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


INSERT INTO public.rounds(name,starts, ends, active, game_settings_id,previous_round_id,next_round_id)
values
    ('Раунд 1','2024-09-01 00:00:00'::TIMESTAMP , '2024-12-31 23:59:59'::TIMESTAMP, true , 1,null,2),
    ('Раунд 2','2025-01-01 00:00:00'::TIMESTAMP , '2025-01-15 23:59:59'::TIMESTAMP, false , 1,1,3),
    ('Раунд 3','2025-01-16 00:00:00'::TIMESTAMP , '2025-01-31 23:59:59'::TIMESTAMP, false , 1,2,4),
    ('Раунд 4','2025-02-01 00:00:00'::TIMESTAMP , '2025-02-15 23:59:59'::TIMESTAMP, false , 1,3,5),
    ('Раунд 5','2025-02-16 00:00:00'::TIMESTAMP , '2025-02-28 23:59:59'::TIMESTAMP, false , 1,4,6),
    ('Раунд 6','2025-03-01 00:00:00'::TIMESTAMP , '2025-03-13 23:59:59'::TIMESTAMP, false , 1,5,7),
    ('Раунд 7','2025-03-14 00:00:00'::TIMESTAMP , '2025-03-31 23:59:59'::TIMESTAMP, false , 1,6,null);

INSERT INTO public.images(is_default,uuid,url,file)
values
    (true,'b3feae74-7915-4ed8-9965-419b9a0a6283'::uuid,'https://aroundgame.ru/api/v1/image/avatar/guest.jpg','guest.jpg'),
    (true,'2da609a9-f54a-4c64-bf8d-88e38cdbc541'::uuid,'https://aroundgame.ru/api/v1/image/width_skill_image.jpg','width_skill_image.jpg'),
    (true,'aba1bbbe-af69-4da6-83ce-572546e2f37e'::uuid,'https://aroundgame.ru/api/v1/image/icon/width_skill_icon.svg','width_skill_icon.svg'),
    (false,'f443a6e6-d81b-4eb6-9af4-bcecda7093f8'::uuid,'https://ucare.timepad.ru/55ad9c96-d442-413d-902b-d953ed5f5d36/-/format/jpeg/-/quality/smart/-/preview/1360x765/',null);

INSERT INTO public.cities(chunks)
Values('[
"8610c221fffffff",
"8610c22f7ffffff",
"8610c22afffffff",
"8610c22d7ffffff",
"8610c228fffffff",
"8610c2287ffffff",
"8610c22b7ffffff",
"8610dc92fffffff",
"8610dc927ffffff",
"8610c229fffffff",
"8610c2297ffffff",
"8610dc907ffffff",
"8610dc937ffffff",
"8610c266fffffff",
"8610dc917ffffff",
"8610c264fffffff"
]' );

INSERT INTO public.users ("level", coins, username, "password", "role", email, verified)
VALUES(1, 0, 'eduard', '$2a$10$yatZBPQE4uvwPoRSwN/8ZuyJXpBY8HTOGlknkfB4dTbfNlREA4UsS',
       'ADMIN', 'eduard.gorshkov.2020@gmail.com', true);

INSERT INTO settings.costs
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

INSERT INTO settings.rules
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

INSERT INTO public.users_rounds_team_city(user_id, round_id, city_id, team_id)
VALUES (1,1,1, 1),
       (1,2,1, 2);

INSERT INTO public.skills
("name", max_level, description, rule_id, cost_id)
VALUES('width', 11, ''::character varying, 0, 0);

INSERT INTO public.users_skills(user_id, skill_id, current_level)
VALUES(1, 1, 0);

INSERT INTO map_events.providers(id, name, url)
VALUES (1,'timepad','dev.timepad.ru');

INSERT INTO chunks(id, round_id,city_id, owner)
values ('8b10c229952afff',1,1,1);

INSERT INTO map_events.categories(id,"name")
values (1,'Вечеринки');

INSERT INTO map_events.events(id,provider_id,"name",starts_at,ends_at,url,verified,active,ad, image_uuid, reward)
values (1,1,'Снежная королева','2024-10-15 18:00:00'::TIMESTAMP,'2024-12-16 22:00:00'::TIMESTAMP,'https://afisha.timepad.ru/event/2999125',true,true, false,'f443a6e6-d81b-4eb6-9af4-bcecda7093f8',1);

INSERT INTO map_events.events_categories(event_id, category_id)
values (1,1);

INSERT INTO map_events.events_chunks(event_id, round_id, city_id, chunk_id)
VALUES (1,1,1,'8b10c229952afff');
