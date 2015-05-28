SELECT * FROM file.v_File

SELECT * from v_user;
SELECT * FROM groups
SELECT * FROM v_group
SELECT * FROM user_group

DELETE FROM User_Group WHERE log_data_id != 6
DELETE FROM Groups WHERE log_data_id = 37

INSERT INTO users(administrative_user_id, last_name, first_name, email_address)
VALUES
('babm2002','Babin','Marc-Antoine','marc-antoine.babin2@Usherbrooke.ca'),
('gonl2401','Gonin','Laurent','Laurent.Gonin@Usherbrooke.ca'),
('abdj2702','Abdelnour','Jerome','jerome.Abdelnour@Usherbrooke.ca'),
('unip1801','Unia','Paulo','paulo.unia@Usherbrooke.ca'),
('latl2302','Latreille','Louis-Antoine','louis.latreille@Usherbrooke.ca'),
('geee9001','Gee','e','e.gee@Usherbrooke.ca'),
('pelv2805','Pelletier','Vincent','Vincent.Pelletier2@Usherbrooke.ca');

INSERT INTO groups (label, description, unique_label, user_id) VALUES ('Etudiants', 'Groupe des étudiants développant système Opus', false, 1);

-- Insertion du groupe d'étudiants dans le groupe étudiants
INSERT INTO user_group (member_id, group_id, user_id)
SELECT u.user_id as member_id, g.group_id, 1 
FROM users u
CROSS JOIN groups g
WHERE g.label = 'Etudiants' 
-- Notre équipe de projet sera dans le groupe Étudiants, les admins en font partis aussi
AND u.administrative_user_id in('pelv2805','latl2302','unip1801', 'abdj2702', 'gonl2401', 'babm2002','supa1234','geee9001'); 

-- Insertion des user de test dans le groupe admin
INSERT INTO user_group (member_id, group_id, user_id)
SELECT u.user_id as member_id, g.group_id, 1 
FROM users u
CROSS JOIN groups g
WHERE g.label = 'Administrateurs Opus' 
AND u.administrative_user_id in('geee9001');


SELECT * 
FROM
	(SELECT c.file_id, c.label, c.user_id, c.registration FROM file.v_file c WHERE (c.user_id = 22)
	UNION 
	(SELECT DISTINCT a.file_id AS file_id, a.label, a.user_id, a.registration 
	FROM file.v_file a 
	JOIN file.v_file_group b 
		ON ((a.file_id = b.file_id) 
			AND (b.group_id IN (SELECT group_id FROM public.get_member_groups_of_user(22)))
			AND (b.can_edit = true 
			OR (b.can_edit = false AND now() >= b.start_valid) -- Only if !retrieve_can_edit
	)))) AS z 
ORDER BY z.label;
