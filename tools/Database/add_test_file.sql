CREATE OR REPLACE FUNCTION file.add_test_file(
     _cip text,
     _isAdmin int,
     _label text,
     _path text,
     _description text
     )
  RETURNS void AS 
  $BODY$
  
DECLARE
_user_id integer;
_file_id integer;
_group_id integer;
BEGIN

  EXECUTE 'SELECT max(user_id) FROM users WHERE administrative_user_id='''||_cip ||'''' INTO _user_id;

  INSERT INTO file.file(label, user_id)
  VALUES (_label, _user_id);

  EXECUTE 'SELECT max(file_id) FROM file.file WHERE label = ''' ||_label|| ''' GROUP BY label'  INTO _file_id;

  INSERT INTO file.version(file_id, description, path, user_id)
  VALUES(_file_id, _description, _path, _user_id);


  IF _isAdmin = 1 THEN
	EXECUTE 'SELECT max(group_id) FROM groups WHERE label = ''Administrateurs Opus'''  INTO _group_id;
  ELSE
	EXECUTE 'SELECT max(group_id) FROM groups WHERE label = ''Etudiants'''  INTO _group_id;
  END IF;
  
  INSERT INTO file.file_group(file_id, group_id, user_id)
  VALUES (_file_id, _group_id, _user_id);
  
END;
$BODY$
  LANGUAGE plpgsql VOLATILE;
ALTER FUNCTION file.add_test_file(text, int, text, text, text)
  OWNER TO postgres;