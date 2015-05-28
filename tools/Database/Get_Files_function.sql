-- Function: file.get_files(integer, boolean, integer)

-- DROP FUNCTION file.get_files(integer, boolean, integer);

CREATE OR REPLACE FUNCTION file.get_files(
    p_user_id integer,
    p_retrieve_can_edit boolean,
    p_file_id integer)
  RETURNS SETOF file.v_file AS
$BODY$declare
query text;
result file.v_file%rowtype;

begin
	if p_user_id > 0 then
		SELECT * FROM
		(SELECT c.file_id, c.label, c.user_id, c.registration FROM file.v_file c WHERE (c.user_id = ' || p_user_id || ') 
		UNION 
		(SELECT DISTINCT a.file_id AS file_id, a.label, a.user_id, a.registration FROM file.v_file a 
			JOIN file.v_file_group b 
				ON ((a.file_id = b.file_id) 
					AND (b.group_id IN (SELECT group_id FROM public.get_member_groups_of_user(' || p_user_id || ')))
					AND (b.can_edit = true 
					OR (b.can_edit = false AND now() >= b.start_valid) -- Only if !retrieve_can_edit
		)))) AS z 
		ORDER BY z.label;

		return query execute query;
	end if;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION file.get_files(integer, boolean, integer)
  OWNER TO postgres;
