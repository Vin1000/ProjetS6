CREATE OR REPLACE FUNCTION file.get_CurrentVersionFiles(
     cip text
     )
  RETURNS TABLE("file_id" integer, "libelle" text, path text, description text) AS
$BODY$
DECLARE
user_id integer;
BEGIN

  EXECUTE 'SELECT max(user_id) FROM users WHERE administrative_user_id='''||cip ||'''' INTO user_id;

  RETURN QUERY
  SELECT c.file_id, c.label as libelle, v.path, v.description
  From file.get_files(user_id, 'false', 0) c
  JOIN file.version v on v.file_id = c.file_id
  JOIN (SELECT max(version_id) as version_id, fvMax.file_id FROM file.version fvMax GROUP BY fvMax.file_id) as m on m.version_id = v.version_id;  
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION file.get_CurrentVersionFiles(text)
  OWNER TO postgres;