CREATE SCHEMA audit;
CREATE SCHEMA content;
CREATE SCHEMA file;
CREATE SCHEMA notification;

COMMENT ON SCHEMA audit IS 'Out-of-table audit/history logging tables and trigger functions';

SET default_tablespace = '';
SET default_with_oids = false;


CREATE EXTENSION IF NOT EXISTS hstore;


CREATE SEQUENCE log_data_id_seq;


CREATE TABLE audit.event (
    event_id bigserial,
    table_schema text NOT NULL,
	table_name text NOT NULL,
	log_data_id integer,
	user_id integer,
    action text NOT NULL CHECK (action IN ('I','D','U', 'T')),
    row_data hstore,
    changed_fields hstore,
	statement_only boolean not null,
	session_user_name text,
	application_name text,
    client_addr inet,
    client_port integer,
    client_query text,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


SET search_path = audit, pg_catalog;


REVOKE ALL ON event FROM public;

COMMENT ON TABLE event IS 'History of auditable actions on audited tables, from audit.if_modified_func()';
COMMENT ON COLUMN event.event_id IS 'Unique identifier for each auditable event';
COMMENT ON COLUMN event.table_schema IS 'Database schema audited table for this event is in';
COMMENT ON COLUMN event.table_name IS 'Non-schema-qualified table name of table event occured in';
COMMENT ON COLUMN event.log_data_id IS 'Unique identifier of row event occured on';
COMMENT ON COLUMN event.user_id IS 'Opus user whose statement caused the audited event';
COMMENT ON COLUMN event.action IS 'Action type; I = insert, D = delete, U = update, T = truncate';
COMMENT ON COLUMN event.row_data IS 'Record value. Null for statement-level trigger. For INSERT this is the new tuple. For DELETE and UPDATE it is the old tuple.';
COMMENT ON COLUMN event.changed_fields IS 'New values of fields changed by UPDATE. Null except for row-level UPDATE events.';
COMMENT ON COLUMN event.statement_only IS '''t'' if audit event is from an FOR EACH STATEMENT trigger, ''f'' for FOR EACH ROW';
COMMENT ON COLUMN event.session_user_name IS 'Login / session user whose statement caused the audited event';
COMMENT ON COLUMN event.client_addr IS 'IP address of client that issued query. Null for unix domain socket.';
COMMENT ON COLUMN event.client_port IS 'Remote peer IP port address of client that issued query. Undefined for unix socket.';
COMMENT ON COLUMN event.application_name IS 'Application name set when this audit event occurred. Can be changed in-session by client.';
COMMENT ON COLUMN event.client_query IS 'Top-level query that caused this auditable event. May be more than one statement.';
COMMENT ON COLUMN event.registration IS 'Transaction start timestamp';


SET search_path = content, pg_catalog;


CREATE TABLE type (
	type_id serial,
	table_schema text NOT NULL,
	table_name text NOT NULL,
    application_id integer,
	url text,
	searchable boolean NOT NULL DEFAULT false,
	is_content boolean NOT NULL DEFAULT false,
	function_name_to_notify_groups_about_an_event text,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


SET search_path = file, pg_catalog;


CREATE TABLE corrected_file (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	corrected_file_id integer NOT NULL,
	file_id integer NOT NULL,
    correction_completed boolean NOT NULL DEFAULT false,
  	user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE file (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    file_id SERIAL,
    label text NOT NULL,
	user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE file_group (
    log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	file_id integer NOT NULL,
    group_id integer NOT NULL,
    can_edit boolean NOT NULL DEFAULT false,
	user_id integer NOT NULL,
	start_valid timestamp with time zone NOT NULL DEFAULT now(),
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE folder (
    log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	folder_id SERIAL,
	parent_id integer,
	label text NOT NULL,
	user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE folder_file (
    log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	folder_id integer NOT NULL,
	file_id integer NOT NULL,
    user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE folder_group (
    log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	folder_id integer NOT NULL,
	group_id integer NOT NULL,
	can_edit boolean NOT NULL DEFAULT false,
	user_id integer NOT NULL,
	start_valid timestamp with time zone NOT NULL DEFAULT now(),
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE turn_in_folder (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    folder_id integer NOT NULL,
	opening_time timestamp with time zone NOT NULL,
    closing_time timestamp with time zone NOT NULL,
	user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE version (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	version_id SERIAL,
	file_id integer NOT NULL,
	description text,
	path text,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


SET search_path = notification, pg_catalog;


CREATE TABLE device (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    device_id SERIAL,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE device_application (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    device_id integer NOT NULL,
	application_id integer NOT NULL,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE device_user_notification (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	device_id integer NOT NULL,
	user_id integer NOT NULL,
	event_id integer NOT NULL,
	sent boolean NOT NULL DEFAULT false,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE email (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	device_id integer NOT NULL,
	email_address text UNIQUE NOT NULL,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE phone (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	device_id integer NOT NULL,
    phone_number text UNIQUE NOT NULL,
	phone_service_provider_id integer NOT NULL,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE phone_service_provider (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	phone_service_provider_id SERIAL,
	email_address_domain_part text UNIQUE NOT NULL,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE user_notification (
    user_id integer NOT NULL,
    event_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE web_gui (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	device_id integer NOT NULL,
	seen boolean NOT NULL DEFAULT false,
	user_id integer UNIQUE NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


SET search_path = public, pg_catalog;


CREATE TABLE administrator_group (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    group_id integer NOT NULL,
    administrator_id integer NOT NULL,
    user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE application (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    application_id SERIAL,
    url text NOT NULL,
    label text NOT NULL,
    description text,
    icon text,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE application_privilege (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    privilege_id integer NOT NULL,
    application_id integer NOT NULL,
    description text,
	user_id integer NOT NULL,
    registration timestamp without time zone DEFAULT now()
);


CREATE TABLE application_privilege_group (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    application_id integer NOT NULL,
	privilege_id integer NOT NULL,
    group_id integer NOT NULL,
    user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE employee (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    user_id integer NOT NULL,
    employee_id text NOT NULL UNIQUE,
    phone_number text,
    office text,
    occupation text,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE groups (
    log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	group_id SERIAL,
    label text NOT NULL,
    description text,
	unique_label boolean DEFAULT false NOT NULL,
	user_id integer NOT NULL,
    last_modification timestamp with time zone,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE group_group (
    log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	group_id integer NOT NULL,
    parent_id integer NOT NULL,
    user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE parameter (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	application_id integer NOT NULL,
	user_id integer NOT NULL,
    parameter_key text NOT NULL,
    parameter_value text NOT NULL,
	registration timestamp without time zone DEFAULT now()
);


CREATE TABLE privilege (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	privilege_id SERIAL,
    label text NOT NULL,
    description text,
	user_id integer NOT NULL,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE profile_picture (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	user_id integer NOT NULL,
    user_agreement boolean NOT NULL DEFAULT false,
	administrator_user_id integer,
    administrator_approval boolean,
    image bytea,
    icon bytea,
	registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE student (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	user_id integer NOT NULL,
    student_id text NOT NULL UNIQUE,
    registration timestamp with time zone NOT NULL DEFAULT now()
);


CREATE TABLE users (
	log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
    user_id SERIAL,
	administrative_user_id text,
    last_name text NOT NULL,
    first_name text NOT NULL,
	gender character(1),
    email_address text NOT NULL,
    last_login timestamp with time zone,
    registration timestamp with time zone NOT NULL DEFAULT now(),
	valid_end timestamp with time zone
);


CREATE TABLE user_group (
    log_data_id integer NOT NULL UNIQUE DEFAULT nextval('public.log_data_id_seq'::regclass),
	member_id integer NOT NULL,
    group_id integer NOT NULL,
	user_id integer NOT NULL,
    registration timestamp with time zone NOT NULL DEFAULT now()
);

SET search_path = audit, pg_catalog;


CREATE UNIQUE INDEX event_pk ON event USING btree (event_id);


SET search_path = content, pg_catalog;


CREATE INDEX type_fk ON type USING btree (application_id);

CREATE UNIQUE INDEX type_pk ON type USING btree (type_id);

CREATE UNIQUE INDEX type_candidate_key ON type USING btree (table_schema, table_name);


SET search_path = file, pg_catalog;


CREATE INDEX corrected_file_fk ON corrected_file USING btree (corrected_file_id);

CREATE INDEX corrected_file_fk2 ON corrected_file USING btree (file_id);

CREATE INDEX corrected_file_fk3 ON corrected_file USING btree (user_id);

CREATE UNIQUE INDEX corrected_file_pk ON corrected_file USING btree (corrected_file_id);

CREATE INDEX file_fk ON corrected_file USING btree (user_id);

CREATE UNIQUE INDEX file_pk ON file USING btree (file_id);

CREATE INDEX file_group_fk ON file_group USING btree (file_id);

CREATE INDEX file_group_fk2 ON file_group USING btree (group_id);

CREATE INDEX file_group_fk3 ON file_group USING btree (user_id);

CREATE UNIQUE INDEX file_group_pk ON file_group USING btree (file_id, group_id);

CREATE INDEX folder_fk ON folder USING btree (parent_id);

CREATE INDEX folder_fk2 ON folder USING btree (user_id);

CREATE UNIQUE INDEX folder_pk ON folder USING btree (folder_id);

CREATE INDEX folder_file_fk ON folder_file USING btree (folder_id);

CREATE INDEX folder_file_fk2 ON folder_file USING btree (file_id);

CREATE INDEX folder_file_fk3 ON folder_file USING btree (user_id);

CREATE UNIQUE INDEX folder_file_pk ON folder_file USING btree (folder_id, file_id);

CREATE INDEX folder_group_fk ON folder_group USING btree (folder_id);

CREATE INDEX folder_group_fk2 ON folder_group USING btree (group_id);

CREATE INDEX folder_group_fk3 ON folder_group USING btree (user_id);

CREATE UNIQUE INDEX folder_group_pk ON folder_group USING btree (folder_id, group_id);

CREATE INDEX turn_in_folder_fk ON turn_in_folder USING btree (folder_id);

CREATE INDEX turn_in_folder_fk2 ON turn_in_folder USING btree (user_id);

CREATE UNIQUE INDEX turn_in_folder_pk ON turn_in_folder USING btree (folder_id);

CREATE INDEX version_fk ON version USING btree (file_id);

CREATE INDEX version_fk2 ON version USING btree (user_id);

CREATE UNIQUE INDEX version_pk ON version USING btree (version_id);
	

SET search_path = notification, pg_catalog;


CREATE UNIQUE INDEX device_pk ON device USING btree (device_id);

CREATE INDEX device_fk ON device USING btree (user_id);

CREATE UNIQUE INDEX device_application_pk ON device_application USING btree (device_id, application_id);

CREATE INDEX device_application_fk ON device_application USING btree (device_id);

CREATE INDEX device_application_fk2 ON device_application USING btree (application_id);

CREATE INDEX device_application_fk3 ON device_application USING btree (user_id);

CREATE UNIQUE INDEX device_user_notification_pk ON device_user_notification USING btree (device_id, event_id);

CREATE INDEX device_user_notification_fk ON device_user_notification USING btree (device_id);

CREATE INDEX device_user_notification_fk3 ON device_user_notification USING btree (user_id);

CREATE INDEX device_user_notification_fk2 ON device_user_notification USING btree (event_id);

CREATE UNIQUE INDEX email_pk ON email USING btree (device_id);

CREATE INDEX email_fk ON email USING btree (user_id);

CREATE UNIQUE INDEX phone_pk ON phone USING btree (device_id);

CREATE INDEX phone_fk ON phone USING btree (user_id);

CREATE INDEX phone_fk2 ON phone USING btree (phone_service_provider_id);

CREATE UNIQUE INDEX phone_service_provider_pk ON phone_service_provider USING btree (phone_service_provider_id);

CREATE INDEX phone_service_provider_fk ON phone_service_provider USING btree (user_id);

CREATE UNIQUE INDEX user_notification_pk ON user_notification USING btree (user_id, event_id);

CREATE INDEX user_notification_fk ON user_notification USING btree (user_id);

CREATE INDEX user_notification_fk2 ON user_notification USING btree (event_id);

CREATE UNIQUE INDEX web_gui_pk ON web_gui USING btree (device_id);

CREATE INDEX web_gui_fk ON web_gui USING btree (user_id);


SET search_path = public, pg_catalog;


CREATE INDEX administrator_group_fk ON administrator_group USING btree (administrator_id);

CREATE INDEX administrator_group_fk2 ON administrator_group USING btree (group_id);

CREATE INDEX administrator_group_fk3 ON administrator_group USING btree (user_id);

CREATE UNIQUE INDEX administrator_group_pk ON administrator_group USING btree (group_id, administrator_id);

CREATE INDEX application_fk ON application USING btree (user_id);

CREATE UNIQUE INDEX application_pk ON application USING btree (application_id);

CREATE INDEX application_privilege_fk ON application_privilege USING btree (user_id);

CREATE INDEX application_privilege_fk2 ON application_privilege USING btree (application_id);

CREATE INDEX application_privilege_fk3 ON application_privilege USING btree (privilege_id);

CREATE UNIQUE INDEX application_privilege_pk ON application_privilege USING btree (application_id, privilege_id);

CREATE INDEX employee_fk ON employee USING btree (user_id);

CREATE UNIQUE INDEX employee_pk ON employee USING btree (user_id);

CREATE INDEX group_group_fk ON group_group USING btree (parent_id);

CREATE INDEX group_group_fk2 ON group_group USING btree (group_id);

CREATE INDEX group_group_fk3 ON group_group USING btree (user_id);

CREATE UNIQUE INDEX group_group_pk ON group_group USING btree (group_id, parent_id);

CREATE UNIQUE INDEX unique_label_idx ON groups USING btree (label) WHERE unique_label;

CREATE INDEX groups_fk ON groups USING btree (user_id);

CREATE UNIQUE INDEX groups_pk ON groups USING btree (group_id);

CREATE INDEX parameter_fk ON parameter USING btree (application_id);

CREATE INDEX parameter_fk2 ON parameter USING btree (user_id);

CREATE UNIQUE INDEX parameter_pk ON parameter USING btree (application_id, parameter_key);

CREATE INDEX privilege_fk ON privilege USING btree (user_id);

CREATE UNIQUE INDEX privilege_pk ON privilege USING btree (privilege_id);

CREATE INDEX application_privilege_group_fk ON application_privilege_group USING btree (privilege_id);

CREATE INDEX application_privilege_group_fk2 ON application_privilege_group USING btree (application_id);

CREATE INDEX application_privilege_group_fk3 ON application_privilege_group USING btree (group_id);

CREATE INDEX application_privilege_group_fk4 ON application_privilege_group USING btree (user_id);

CREATE INDEX student_fk ON student USING btree (user_id);

CREATE UNIQUE INDEX student_pk ON student USING btree (user_id);

CREATE INDEX profile_picture_fk ON profile_picture USING btree (user_id);

CREATE UNIQUE INDEX profile_picture_pk ON profile_picture USING btree (user_id);

CREATE INDEX user_group_fk ON user_group USING btree (member_id);

CREATE INDEX user_group_fk2 ON user_group USING btree (group_id);

CREATE INDEX user_group_fk3 ON user_group USING btree (user_id);

CREATE UNIQUE INDEX user_group_pk ON user_group USING btree (member_id, group_id);

CREATE UNIQUE INDEX users_pk ON users USING btree (user_id);

-- ********************************* PRIMARY KEYS ********************************* --

SET search_path = audit, pg_catalog;


ALTER TABLE ONLY event
    ADD CONSTRAINT pk_event PRIMARY KEY (event_id);


SET search_path = content, pg_catalog;


ALTER TABLE ONLY type
    ADD CONSTRAINT pk_type PRIMARY KEY (type_id);
	
	
SET search_path = file, pg_catalog;


ALTER TABLE ONLY corrected_file
    ADD CONSTRAINT pk_corrected_file PRIMARY KEY (corrected_file_id);

ALTER TABLE ONLY file
    ADD CONSTRAINT pk_file PRIMARY KEY (file_id);
	
ALTER TABLE ONLY folder
    ADD CONSTRAINT pk_folder PRIMARY KEY (folder_id);

ALTER TABLE ONLY folder_group
    ADD CONSTRAINT pk_folder_group PRIMARY KEY (folder_id, group_id);

ALTER TABLE ONLY folder_file
    ADD CONSTRAINT pk_folder_file PRIMARY KEY (folder_id, file_id);

ALTER TABLE ONLY file_group
    ADD CONSTRAINT pk_file_group PRIMARY KEY (file_id, group_id);

ALTER TABLE ONLY version
    ADD CONSTRAINT pk_version PRIMARY KEY (version_id);

ALTER TABLE ONLY turn_in_folder
    ADD CONSTRAINT pk_turn_in_folder PRIMARY KEY (folder_id);
	

SET search_path = notification, pg_catalog;


ALTER TABLE ONLY device
    ADD CONSTRAINT pk_device PRIMARY KEY (device_id);
	
ALTER TABLE ONLY device_application
    ADD CONSTRAINT pk_device_application PRIMARY KEY (device_id, application_id);

ALTER TABLE ONLY device_user_notification
    ADD CONSTRAINT pk_device_user_notification PRIMARY KEY (device_id, event_id);	

ALTER TABLE ONLY email
    ADD CONSTRAINT pk_email PRIMARY KEY (device_id);	

ALTER TABLE ONLY phone
    ADD CONSTRAINT pk_phone PRIMARY KEY (device_id);
	
ALTER TABLE ONLY phone_service_provider
    ADD CONSTRAINT pk_phone_service_provider PRIMARY KEY (phone_service_provider_id);	

ALTER TABLE ONLY user_notification
    ADD CONSTRAINT pk_user_notification PRIMARY KEY (user_id, event_id);
	
ALTER TABLE ONLY web_gui
    ADD CONSTRAINT pk_web_gui PRIMARY KEY (device_id);


SET search_path = public, pg_catalog;


ALTER TABLE ONLY application
    ADD CONSTRAINT pk_application PRIMARY KEY (application_id);
	
ALTER TABLE ONLY application_privilege
    ADD CONSTRAINT pk_application_privilege PRIMARY KEY (privilege_id, application_id);
	
ALTER TABLE ONLY application_privilege_group
    ADD CONSTRAINT pk_privilege_group PRIMARY KEY (application_id, privilege_id, group_id);
	
ALTER TABLE ONLY administrator_group
    ADD CONSTRAINT pk_administrator_group PRIMARY KEY (group_id, administrator_id);

ALTER TABLE ONLY employee
    ADD CONSTRAINT pk_employee PRIMARY KEY (user_id);
	
ALTER TABLE ONLY groups
    ADD CONSTRAINT pk_group PRIMARY KEY (group_id);
	
ALTER TABLE ONLY group_group
    ADD CONSTRAINT pk_group_group PRIMARY KEY (group_id, parent_id);

ALTER TABLE ONLY parameter
    ADD CONSTRAINT pk_parameter PRIMARY KEY (application_id, parameter_key);
	
ALTER TABLE ONLY privilege
    ADD CONSTRAINT pk_privilege PRIMARY KEY (privilege_id);

ALTER TABLE ONLY profile_picture
    ADD CONSTRAINT pk_profile_picture PRIMARY KEY (user_id);

ALTER TABLE ONLY student
    ADD CONSTRAINT pk_student PRIMARY KEY (user_id);
	
ALTER TABLE ONLY users
    ADD CONSTRAINT pk_users PRIMARY KEY (user_id);
	
ALTER TABLE ONLY user_group
    ADD CONSTRAINT pk_user_group PRIMARY KEY (member_id, group_id);

	
-- ********************************* FOREIGN KEYS ********************************* --


SET search_path = file, pg_catalog;


ALTER TABLE ONLY corrected_file
    ADD CONSTRAINT fk_corrected_f_links_corrected_file FOREIGN KEY (corrected_file_id) REFERENCES file(file_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY corrected_file
    ADD CONSTRAINT fk_corrected_f_links_file FOREIGN KEY (file_id) REFERENCES file(file_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY corrected_file
    ADD CONSTRAINT fk_corrected_f_added_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY file
    ADD CONSTRAINT fk_file_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY file_group
    ADD CONSTRAINT fk_file_g_links_file FOREIGN KEY (file_id) REFERENCES file(file_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY file_group
    ADD CONSTRAINT fk_file_g_links_group FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY file_group
    ADD CONSTRAINT fk_file_g_added_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY folder
    ADD CONSTRAINT fk_folder_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY folder
    ADD CONSTRAINT fk_folder_has_parent FOREIGN KEY (parent_id) REFERENCES folder(folder_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY folder_file
    ADD CONSTRAINT fk_folder_f_links_folder FOREIGN KEY (folder_id) REFERENCES folder(folder_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY folder_file
    ADD CONSTRAINT fk_folder_f_links_file FOREIGN KEY (file_id) REFERENCES file(file_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY folder_file
    ADD CONSTRAINT fk_folder_f_added_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY folder_group
    ADD CONSTRAINT fk_folder_g_links_folder FOREIGN KEY (folder_id) REFERENCES folder(folder_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY folder_group
    ADD CONSTRAINT fk_folder_g_links_group FOREIGN KEY (group_id) REFERENCES public.groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY folder_group
    ADD CONSTRAINT fk_folder_g_added_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY turn_in_folder
    ADD CONSTRAINT fk_turn_i_f_links_folder FOREIGN KEY (folder_id) REFERENCES folder(folder_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY turn_in_folder
    ADD CONSTRAINT fk_turn_i_f_added_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY version
    ADD CONSTRAINT fk_version_is_a_revision_of_file FOREIGN KEY (file_id) REFERENCES file(file_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY version
    ADD CONSTRAINT fk_version_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;	
	
	
SET search_path = notification, pg_catalog;

ALTER TABLE ONLY device
    ADD CONSTRAINT fk_device_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY device_application
    ADD CONSTRAINT fk_device_a_links_device FOREIGN KEY (device_id) REFERENCES device(device_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY device_application
    ADD CONSTRAINT fk_device_a_links_application FOREIGN KEY (application_id) REFERENCES public.application(application_id) ON UPDATE CASCADE ON DELETE CASCADE;
	
ALTER TABLE ONLY device_application
    ADD CONSTRAINT fk_device_a_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY device_user_notification
    ADD CONSTRAINT fk_device_u_n_links_device FOREIGN KEY (device_id) REFERENCES device(device_id) ON UPDATE CASCADE ON DELETE CASCADE;
	
ALTER TABLE ONLY device_user_notification
    ADD CONSTRAINT fk_device_u_n_links_event FOREIGN KEY (user_id, event_id) REFERENCES user_notification(user_id, event_id) ON UPDATE CASCADE ON DELETE CASCADE;	
	
ALTER TABLE ONLY email
    ADD CONSTRAINT fk_email_links_device FOREIGN KEY (device_id) REFERENCES device(device_id) ON UPDATE CASCADE ON DELETE CASCADE;
	
ALTER TABLE ONLY email
    ADD CONSTRAINT fk_email_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY phone
    ADD CONSTRAINT fk_phone_links_device FOREIGN KEY (device_id) REFERENCES device(device_id) ON UPDATE CASCADE ON DELETE CASCADE;
	
ALTER TABLE ONLY phone
    ADD CONSTRAINT fk_phone_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY phone
    ADD CONSTRAINT fk_phone_links_phone_service_provider FOREIGN KEY (phone_service_provider_id) REFERENCES phone_service_provider(phone_service_provider_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY phone_service_provider
    ADD CONSTRAINT fk_phone_s_p_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY user_notification
    ADD CONSTRAINT fk_user_n_links_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY user_notification
    ADD CONSTRAINT fk_user_n_links_event FOREIGN KEY (event_id) REFERENCES audit.event(event_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY web_gui
    ADD CONSTRAINT fk_web_g_links_device FOREIGN KEY (device_id) REFERENCES device(device_id) ON UPDATE CASCADE ON DELETE CASCADE;
	
ALTER TABLE ONLY web_gui
    ADD CONSTRAINT fk_web_g_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;


SET search_path = public, pg_catalog;


ALTER TABLE ONLY administrator_group
    ADD CONSTRAINT fk_group_a_g_links_administrated_group FOREIGN KEY (group_id) REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY administrator_group
    ADD CONSTRAINT fk_group_a_g_links_administrator_group FOREIGN KEY (administrator_id) REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY administrator_group
    ADD CONSTRAINT fk_group_a_g_links_added_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY application
    ADD CONSTRAINT fk_application_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;	

ALTER TABLE ONLY application_privilege
    ADD CONSTRAINT fk_application_p_links_application FOREIGN KEY (application_id) REFERENCES application(application_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY application_privilege
    ADD CONSTRAINT fk_application_p_links_privilege FOREIGN KEY (privilege_id) REFERENCES privilege(privilege_id) ON UPDATE CASCADE ON DELETE CASCADE;	

ALTER TABLE ONLY application_privilege
    ADD CONSTRAINT fk_application_p_added_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;	
	
ALTER TABLE ONLY application_privilege_group
    ADD CONSTRAINT fk_application_p_g_links_application_privilege FOREIGN KEY (application_id, privilege_id) REFERENCES application_privilege(application_id, privilege_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY application_privilege_group
    ADD CONSTRAINT fk_application_p_g_links_group FOREIGN KEY (group_id) REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY application_privilege_group
    ADD CONSTRAINT fk_application_p_g_links_added_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY employee
    ADD CONSTRAINT fk_employee_is_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY groups
    ADD CONSTRAINT fk_groups_created_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY group_group
    ADD CONSTRAINT fk_group_g_links_child FOREIGN KEY (group_id) REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY group_group
    ADD CONSTRAINT fk_group_g_links_parent FOREIGN KEY (parent_id) REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY group_group
    ADD CONSTRAINT fk_group_g_added_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY parameter
    ADD CONSTRAINT fk_parameter_owned_by_application FOREIGN KEY (application_id) REFERENCES application(application_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY parameter
    ADD CONSTRAINT fk_parameter_created_by_user FOREIGN KEY (user_id) REFERENCES public.users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY privilege
    ADD CONSTRAINT fk_privilege_created_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY profile_picture
    ADD CONSTRAINT fk_profile_p_created_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY profile_picture
    ADD CONSTRAINT fk_profile_p_approved_by_administrator FOREIGN KEY (administrator_user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

ALTER TABLE ONLY student
    ADD CONSTRAINT fk_student_is_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY user_group
    ADD CONSTRAINT fk_user_g_links_group FOREIGN KEY (group_id) REFERENCES groups(group_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY user_group
    ADD CONSTRAINT fk_user_g_links_member FOREIGN KEY (member_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE ONLY user_group
    ADD CONSTRAINT fk_user_g_added_by_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON UPDATE CASCADE ON DELETE RESTRICT;

	
SET search_path = content, pg_catalog;


CREATE VIEW v_type AS
 SELECT type.type_id,
	type.table_schema,
	type.table_name,
	type.application_id,
	type.url,
	type.function_name_to_notify_groups_about_an_event
   FROM content.type;   


SET search_path = file, pg_catalog;


CREATE VIEW v_corrected_file AS 
 SELECT corrected_file.corrected_file_id,
	corrected_file.file_id,
	corrected_file.correction_completed,
	corrected_file.user_id
   FROM corrected_file;

CREATE VIEW v_file AS
 SELECT file.file_id,
    file.label,
	file.user_id,
    file.registration   
   FROM file;
  
  
CREATE VIEW v_file_group AS 
 SELECT file_group.file_id,
    file_group.group_id,
    file_group.can_edit,
	file_group.user_id,
	file_group.start_valid 
   FROM file_group;


CREATE VIEW v_folder AS
 WITH RECURSIVE everyfolder AS (
                 SELECT folder.parent_id AS node_id,
                    folder.parent_id,
                    folder.folder_id,
                    folder.label,
					folder.user_id,
                    1 AS depth,
                    ARRAY[folder.parent_id] AS path
                   FROM folder
        UNION ALL
                 SELECT every.node_id,
                    subfolder.parent_id,
                    subfolder.folder_id,
                    subfolder.label,
					subfolder.user_id,
                    (every.depth + 1),
                    (every.path || subfolder.parent_id) AS path
                   FROM (folder subfolder
              JOIN everyfolder every ON ((subfolder.parent_id = every.folder_id)))
        )
 SELECT everyfolder.node_id,
    everyfolder.parent_id,
    everyfolder.folder_id,
	everyfolder.label,
    everyfolder.user_id,
    everyfolder.depth,
    everyfolder.path
   FROM everyfolder
  ORDER BY everyfolder.node_id;

  
CREATE VIEW v_folder_file AS 
 SELECT folder_file.folder_id,
	folder_file.file_id,
    folder_file.user_id
   FROM folder_file;


CREATE VIEW v_folder_group AS
 SELECT folder_group.folder_id,
	folder_group.group_id,
	folder_group.can_edit,
	folder_group.user_id,
	folder_group.start_valid  
   FROM folder_group;


CREATE VIEW v_turn_in_folder AS
 SELECT turn_in_folder.folder_id,
	turn_in_folder.opening_time,
    turn_in_folder.closing_time,
	turn_in_folder.user_id
   FROM turn_in_folder;


CREATE VIEW v_version AS
 SELECT version.version_id,
	version.file_id,
	version.description,
    version.path,
	version.user_id,
	version.registration
   FROM version;

   
SET search_path = notification, pg_catalog;


CREATE VIEW v_device AS
 SELECT device.device_id,
    device.user_id
   FROM device;


CREATE VIEW v_device_application AS
 SELECT device_application.device_id,
	device_application.application_id,
	device_application.user_id 
   FROM device_application;

   
CREATE VIEW v_device_user_notification AS 
 SELECT device_user_notification.device_id,
	device_user_notification.user_id,
	device_user_notification.event_id,
	device_user_notification.sent
   FROM device_user_notification; 
   
   
CREATE VIEW v_email AS
 SELECT email.device_id,
	email.user_id, 
	email.email_address 
   FROM email;
   

CREATE VIEW v_phone AS
 SELECT phone.device_id,
	phone.user_id,
    phone.phone_number,
	phone.phone_service_provider_id 
   FROM phone;


CREATE VIEW v_phone_service_provider AS
 SELECT phone_service_provider.phone_service_provider_id,
	phone_service_provider.email_address_domain_part,
	phone_service_provider.user_id
   FROM phone_service_provider;


CREATE VIEW v_user_notification AS
 SELECT user_notification.user_id,
    user_notification.event_id,
    user_notification.registration
   FROM user_notification;
   

CREATE VIEW v_web_gui AS
 SELECT web_gui.device_id,
	web_gui.user_id
   FROM web_gui;
   
   
SET search_path = public, pg_catalog;


CREATE VIEW v_application AS
 SELECT application.application_id,
    application.url,
    application.label,
    application.description,
	application.icon,
	application.user_id,
    application.registration
   FROM application;
  
 
CREATE OR REPLACE VIEW v_application_privilege AS 
 SELECT application_privilege.application_id,
    application_privilege.privilege_id,
	application_privilege.description,
    application_privilege.user_id
   FROM application_privilege;
 

CREATE VIEW v_application_privilege_group AS
 SELECT application_privilege_group.application_id,
	application_privilege_group.privilege_id,
    application_privilege_group.group_id,
    application_privilege_group.user_id,
	application_privilege_group.registration
   FROM application_privilege_group;


CREATE VIEW v_administrator_group AS
 SELECT DISTINCT administrator_group.group_id,
    administrator_group.administrator_id,
    administrator_group.user_id
   FROM administrator_group
  ORDER BY administrator_group.administrator_id;


CREATE VIEW v_employee AS
	SELECT employee.user_id,
		employee.employee_id,
		employee.phone_number,
		employee.office,
		employee.occupation
	FROM public.employee;
  

CREATE VIEW v_group AS
 SELECT groups.group_id,
    groups.label,
    groups.description,
	groups.unique_label,
	groups.user_id,
	groups.registration
   FROM groups;
  

CREATE VIEW v_group_group AS
 SELECT DISTINCT group_group.parent_id,
    group_group.group_id,
    group_group.user_id
   FROM group_group
  ORDER BY group_group.parent_id;


CREATE VIEW v_group_group_node AS
 WITH RECURSIVE everygroup AS (
                 SELECT group_group.parent_id AS node_id,
                    group_group.parent_id,
                    group_group.group_id,
                    group_group.user_id,
                    1 AS depth,
                    ARRAY[group_group.parent_id] AS path
                   FROM group_group
        UNION ALL
                 SELECT every.node_id,
                    subgroup.parent_id,
                    subgroup.group_id,
                    subgroup.user_id,
                    (every.depth + 1),
                    (every.path || subgroup.parent_id) AS path
                   FROM (group_group subgroup
              JOIN everygroup every ON ((subgroup.parent_id = every.group_id)))
        )
 SELECT everygroup.node_id,
    everygroup.parent_id,
    everygroup.group_id,
    everygroup.user_id,
    everygroup.depth,
    everygroup.path
   FROM everygroup
  ORDER BY everygroup.node_id;


CREATE VIEW v_parameter AS
 SELECT application_id,
	  user_id,
      parameter_key,
      parameter_value
	FROM parameter;


CREATE VIEW v_privilege AS
 SELECT privilege.privilege_id,
	privilege.user_id,
    privilege.label,
    privilege.description,
	privilege.registration
   FROM privilege;
 

CREATE VIEW v_profile_picture AS
 SELECT profile_picture.user_id,
    profile_picture.user_agreement,
	profile_picture.administrator_user_id,
    profile_picture.administrator_approval,
    profile_picture.image,
    profile_picture.icon,
	profile_picture.registration
   FROM profile_picture;


CREATE VIEW v_student AS
 SELECT student.user_id,
	student.student_id
   FROM public.student;
   

CREATE VIEW v_user AS
 SELECT users.user_id,
	users.administrative_user_id,
    users.last_name,
    users.first_name,
    users.email_address,
	users.gender,
    users.last_login,
	users.registration,
	users.valid_end
   FROM users;

   
CREATE VIEW v_user_group AS
 SELECT user_group.group_id,
    groups.label,
    user_group.member_id,
    user_group.user_id,
    user_group.registration
   FROM (user_group
   JOIN groups ON ((user_group.group_id = groups.group_id)));


CREATE VIEW v_user_group_node AS
 SELECT DISTINCT ON (e.member_id, e.group_id) e.node_id, e.group_id, e.member_id, e.user_id
   FROM ( SELECT DISTINCT b.node_id, c.group_id, a.member_id, a.user_id
            FROM v_user_group a
            JOIN v_group_group_node b ON a.group_id = b.group_id
			JOIN v_group c ON (c.group_id = ANY (b.path)) OR c.group_id = a.group_id
		  UNION 
          SELECT DISTINCT d.group_id AS node_id, d.group_id, d.member_id, d.user_id
			FROM v_user_group d) e;

SET search_path = audit, pg_catalog;

CREATE FUNCTION if_modified_func() RETURNS TRIGGER AS $body$
DECLARE
    audit_row audit.event;
    excluded_cols text[] = ARRAY[]::text[];
	
BEGIN
    IF TG_WHEN <> 'AFTER' THEN
        RAISE EXCEPTION 'audit.if_modified_func() may only run as an AFTER trigger';
    END IF;

    audit_row = ROW(
        nextval('audit.event_event_id_seq'),          -- event_id
        TG_TABLE_SCHEMA::text,                        -- schema_name
        TG_TABLE_NAME::text,                          -- table_name
        NULL, NULL,                                   -- log_data_id, Opus user_id
        substring(TG_OP,1,1),                         -- action
        NULL, NULL,                                   -- row_data, changed_fields
		'f',                                          -- statement_only
		session_user::text,                           -- session_user_name
		current_setting('application_name'),          -- client application
        inet_client_addr(),                           -- client_addr
        inet_client_port(),                           -- client_port
        current_query(),                              -- top-level query or queries (if multistatement) from client
		now()                                         -- registration timestamp
    );

    IF NOT TG_ARGV[0]::boolean IS DISTINCT FROM 'f'::boolean THEN
        audit_row.client_query = NULL;
    END IF;

    IF TG_ARGV[1] IS NOT NULL THEN
        excluded_cols = TG_ARGV[1]::text[];
    END IF;
    
	IF(TG_OP IN ('INSERT','UPDATE','DELETE','TRUNCATE')) THEN
		IF (TG_LEVEL = 'ROW') THEN
			audit_row.statement_only = 'f';
			
			IF (TG_OP = 'UPDATE') THEN
				audit_row.row_data = hstore(OLD.*);
				audit_row.changed_fields = (hstore(NEW.*) - audit_row.row_data) - excluded_cols;
				audit_row.log_data_id = NEW.log_data_id;
				audit_row.user_id = NEW.user_id;
				
				IF audit_row.changed_fields = hstore('') THEN
					-- All changed fields are ignored. Skip this update.
					RETURN NULL;
				END IF;
			ELSIF (TG_OP = 'DELETE' OR TG_OP = 'TRUNCATE') THEN
				audit_row.row_data = hstore(OLD.*) - excluded_cols;
				audit_row.log_data_id = OLD.log_data_id;
				audit_row.user_id = OLD.user_id;
			ELSIF (TG_OP = 'INSERT') THEN
				audit_row.row_data = hstore(NEW.*) - excluded_cols;
				audit_row.log_data_id = NEW.log_data_id;
				audit_row.user_id = NEW.user_id;
			END IF;
		
		ELSIF (TG_LEVEL = 'STATEMENT') THEN
			audit_row.statement_only = 't';
		ELSE
			RAISE EXCEPTION '[audit.if_modified_func] - Trigger func added as trigger for unhandled case: %, %',TG_OP, TG_LEVEL;
			RETURN NULL;
		END IF;
		
		INSERT INTO audit.event VALUES (audit_row.*);
	END IF;
	
    RETURN NULL;
END;
$body$
LANGUAGE plpgsql;


COMMENT ON FUNCTION if_modified_func() IS $body$
Track changes to a table at the statement and/or row level.

Optional parameters to trigger in CREATE TRIGGER call:

param 0: boolean, whether to log the query text. Default 't'.

param 1: text[], columns to ignore in updates. Default [].

         Updates to ignored cols are omitted from changed_fields.

         Updates with only ignored cols changed are not inserted
         into the audit log.

         Almost all the processing work is still done for updates
         that ignored. If you need to save the load, you need to use
         WHEN clause on the trigger instead.

         No warning or error is issued if ignored_cols contains columns
         that do not exist in the target table. This lets you specify
         a standard set of ignored columns.

There is no parameter to disable logging of values. Add this trigger as
a 'FOR EACH STATEMENT' rather than 'FOR EACH ROW' trigger if you do not
want to log row values.
$body$;


CREATE FUNCTION audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean, ignored_cols text[]) RETURNS void AS $body$
DECLARE
  stm_targets text = 'INSERT OR UPDATE OR DELETE OR TRUNCATE';
  _q_txt text;
  _ignored_cols_snip text = '';
BEGIN
    EXECUTE 'DROP TRIGGER IF EXISTS audit_trigger_row ON ' || target_table;
    EXECUTE 'DROP TRIGGER IF EXISTS audit_trigger_stm ON ' || target_table;

    IF audit_rows THEN
        IF array_length(ignored_cols,1) > 0 THEN
            _ignored_cols_snip = ', ' || quote_literal(ignored_cols);
        END IF;
        _q_txt = 'CREATE TRIGGER audit_trigger_row AFTER INSERT OR UPDATE OR DELETE ON ' || 
                 target_table || 
                 ' FOR EACH ROW EXECUTE PROCEDURE audit.if_modified_func(' ||
                 quote_literal(audit_query_text) || _ignored_cols_snip || ');';
        RAISE NOTICE '%',_q_txt;
        EXECUTE _q_txt;
        stm_targets = 'TRUNCATE';
    END IF;

    _q_txt = 'CREATE TRIGGER audit_trigger_stm AFTER ' || stm_targets || ' ON ' ||
             target_table ||
             ' FOR EACH STATEMENT EXECUTE PROCEDURE audit.if_modified_func('||
             quote_literal(audit_query_text) || ');';
    RAISE NOTICE '%',_q_txt;
    EXECUTE _q_txt;

END;
$body$
language 'plpgsql';


COMMENT ON FUNCTION audit_table(regclass, boolean, boolean, text[]) IS $body$
Add auditing support to a table.

Arguments:
   target_table:     Table name, schema qualified if not on search_path
   audit_rows:       Record each row change, or only audit at a statement level
   audit_query_text: Record the text of the client query that triggered the audit event?
   ignored_cols:     Columns to exclude from update diffs, ignore updates that change only ignored cols.
$body$;


-- Pg doesn't allow variadic calls with 0 params, so provide a wrapper
CREATE FUNCTION audit_table(target_table regclass, audit_rows boolean, audit_query_text boolean) RETURNS void AS $body$
SELECT audit.audit_table($1, $2, $3, ARRAY[]::text[]);
$body$ LANGUAGE SQL;


-- And provide a convenience call wrapper for the simplest case
-- of row-level logging with no excluded cols and query logging enabled.
--
CREATE FUNCTION audit_table(target_table regclass) RETURNS void AS $$
SELECT audit.audit_table($1, BOOLEAN 't', BOOLEAN 't');
$$ LANGUAGE 'sql';


COMMENT ON FUNCTION audit_table(regclass) IS $body$
Add auditing support to the given table. Row-level changes will be logged with full client query text. No cols are ignored.
$body$;


SET search_path = file, pg_catalog;


CREATE FUNCTION get_files(p_user_id integer, p_retrieve_can_edit boolean, p_file_id integer)
  RETURNS SETOF file.v_file AS
$$declare
query text;
result file.v_file%rowtype;

begin
	if p_user_id > 0 then
		query := 'SELECT * FROM (SELECT c.file_id, c.label, c.user_id, c.registration FROM file.v_file c WHERE (c.user_id = ' || p_user_id || ') UNION (SELECT DISTINCT a.file_id AS file_id, a.label, a.user_id, a.registration FROM file.v_file a JOIN file.v_file_group b ON ((a.file_id = b.file_id) AND (b.group_id IN (SELECT group_id FROM public.get_member_groups_of_user(' || p_user_id || '))) ';

		query := query || 'AND (b.can_edit = true ';
		
		if not p_retrieve_can_edit then
			query := query || 'OR (b.can_edit = false AND now() >= b.start_valid) ';
		end if;

		query := query || ')))) AS z ';

		if p_file_id > 0 then
			query := query || 'WHERE z.file_id = ' || p_file_id || ' ';
		end if;

		query := query || 'ORDER BY z.label';

		return query execute query;
	end if;
END;
$$ LANGUAGE plpgsql;


CREATE FUNCTION get_folders(p_user_id integer, p_retrieve_can_edit boolean, p_retrieve_subfolders boolean, p_folder_id integer, p_parent_id integer)
  RETURNS SETOF file.v_folder AS
$$declare
query text;

result file.v_folder%rowtype;
first_condition_added boolean := false;

begin
	if p_user_id > 0 then
		query := 'SELECT * FROM (SELECT c.folder_id, c.parent_id, c.label, c.user_id, c.registration AS registration FROM file.v_folder c WHERE (c.user_id = ' || p_user_id || ') UNION (SELECT DISTINCT a.folder_id, a.label, a.user_id, a.registration FROM file.v_folder a JOIN file.v_folder_group b ON ((a.folder_id = b.folder_id) AND (b.group_id IN (SELECT group_id FROM public.get_member_groups_of_user(' || p_user_id || '))) ';

		query := query || 'AND (b.can_edit = true ';
		
		if not p_retrieve_only_can_edit then
			query := query || 'OR (b.can_edit = false AND now() >= b.start_valid) ';
		end if;

		query := query || ')))) AS z ';
		
		if p_retrieve_subfolders then
			if p_parent_id > 0 then
				query := query || 'WHERE z.parent_id = ' || p_parent_id || ' ';
				first_condition_added := true;
			end if;
		else
			query := query || 'WHERE z.parent_id IS NULL ';
			first_condition_added := true;
		end if;
		
		if p_folder_id > 0 then
			if first_condition_added then
				query := query || 'AND ';
			else
				query := query || 'WHERE ';
			end if;
			
			query := query || 'z.folder_id = ' || p_folder_id || ' ';
		end if;

		query := query || 'ORDER BY z.label';

		return query execute query;
	end if;
END;
$$ LANGUAGE plpgsql;


CREATE FUNCTION get_groups_to_notify_about_file(p_schema_and_table_name text, p_log_data_id integer) RETURNS integer[]
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
related_file_id integer;

BEGIN

EXECUTE 'SELECT file_id FROM ' || p_schema_and_table_name || ' WHERE log_data_id = ' || p_log_data_id INTO related_file_id;

list_group_id := ARRAY(SELECT DISTINCT group_id
						FROM file.file_group
						WHERE file_id = related_file_id);
						
RETURN list_group_id;

END;$$;


CREATE FUNCTION get_groups_to_notify_about_folder(p_schema_and_table_name text, p_log_data_id integer) RETURNS integer[]
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
related_folder_id integer;

BEGIN

EXECUTE 'SELECT folder_id FROM ' || p_schema_and_table_name || ' WHERE log_data_id = ' || p_log_data_id INTO related_folder_id;

list_group_id := ARRAY(SELECT DISTINCT group_id
						FROM file.folder_group
						WHERE folder_id = related_folder_id);
						
RETURN list_group_id;

END;$$;


CREATE FUNCTION get_turn_in_folders(p_user_id integer)
  RETURNS SETOF file.v_turn_in_folder AS
$$declare
query text;
result file.v_turn_in_folder%rowtype;

begin
	if p_user_id > 0 then
		query := 'SELECT * FROM (SELECT c.folder_id, c.opening_time_id, c.closing_time, c.user_id FROM file.v_turn_in_folder c WHERE (c.user_id = ' || p_user_id || ') UNION (SELECT DISTINCT a.folder_id, a.opening_time, a.closing_time, a.user_id FROM file.v_turn_in_folder a JOIN file.v_folder_group b ON ((a.folder_id = b.folder_id) AND (b.group_id IN (SELECT group_id FROM public.get_member_groups_of_user(' || p_user_id || ')))))) AS z ORDER BY z.label';

		return query execute query;
	end if;
END;
$$ LANGUAGE plpgsql;


SET search_path = notification, pg_catalog;


CREATE FUNCTION get_user_notification(p_user_id integer, p_list_notification_id_already_loaded text, p_limit integer) RETURNS SETOF v_user_notification
    AS $$declare
query text;

begin
	query := 'SELECT * FROM notification.v_user_notification z WHERE (z.user_id = ' || p_user_id || ' ';

	if (p_list_notification_id_already_loaded <> '()') then
		query := query || 'AND z.event_id NOT IN ' || p_list_notification_id_already_loaded || ' ';
	end if;

	query := query || ') ORDER BY z.registration DESC ';

	if (p_limit > 0) then 
		query := query || 'LIMIT ' || p_limit || ' ';
	end if;

	query := query || ';';

	return query execute query;
END;
$$ LANGUAGE plpgsql;


CREATE FUNCTION notify_user(p_event_id bigint, p_user_id integer) RETURNS void
    AS $$

BEGIN
	IF(p_event_id IS NOT NULL AND p_user_id IS NOT NULL) THEN
		INSERT INTO notification.user_notification (user_id, event_id) (SELECT p_user_id AS user_id, p_event_id AS event_id
																			WHERE NOT EXISTS (SELECT 1 
																								FROM notification.user_notification 
																								WHERE user_id = p_user_id AND event_id = p_event_id));
	END IF;
END;
$$ LANGUAGE plpgsql;


SET search_path = public, pg_catalog;


CREATE FUNCTION get_administrated_groups_by_user(p_user_id integer) RETURNS SETOF v_group
    AS $$declare
query text;

begin

if p_user_id > 0 then
	
	query := 'SELECT DISTINCT * FROM (SELECT c.group_id, c.label, c.description, c.unique_label, c.user_id, c.registration FROM public.v_administrator_group a JOIN public.v_user_group_node b ON a.administrator_id = b.group_id AND b.member_id = ' || p_user_id || ' JOIN public.v_group c ON a.group_id = c.group_id';
	query := query || ' UNION SELECT d.group_id, d.label, d.description, d.unique_label, d.user_id, d.registration FROM public.v_group d WHERE d.user_id = ' || p_user_id || ') z ORDER BY z.label';

	if query <> '' then
		return query execute query;
	end if;
end if;
END;
$$ LANGUAGE plpgsql;


CREATE FUNCTION get_application_privilege_groups_of_user(p_user_id integer, p_application_id integer)
  RETURNS SETOF v_application_privilege_group AS
$$declare
query text;

begin
	if p_user_id > 0 then
		query := 'SELECT * FROM (SELECT a.application_id, a.privilege_id, a.group_id, a.user_id, a.registration FROM public.v_application_privilege_group a JOIN public.v_user_group_node b ON (a.group_id = b.group_id) WHERE (b.member_id = ' || p_user_id;

		if p_application_id > 0 then
			query := query || ' AND a.application_id = ' || p_application_id;
		end if;

		query := query || ')) z ORDER BY z.application_id;';
		
		return query execute query;
	end if;
END;
$$ LANGUAGE plpgsql; 


CREATE FUNCTION get_groups_to_notify_about_application_privilege(p_schema_and_table_name text, p_log_data_id integer) RETURNS integer[]
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
related_application_id integer;
related_privilege_id integer;

BEGIN
	EXECUTE 'SELECT application_id, privilege_id FROM ' || p_schema_and_table_name || ' WHERE log_data_id = ' || p_log_data_id INTO related_application_id, related_privilege_id;

	list_group_id := ARRAY(SELECT DISTINCT group_id
							FROM public.application_privilege_group
							WHERE application_id = related_application_id AND privilege_id = related_privilege_id);
	
	RETURN list_group_id;
END;$$;


CREATE FUNCTION get_groups_to_notify_about_group(p_schema_and_table_name text, p_log_data_id integer) RETURNS integer[]
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
related_group_id integer;

BEGIN
	EXECUTE 'SELECT group_id FROM ' || p_schema_and_table_name || ' WHERE log_data_id = ' || p_log_data_id INTO related_group_id;

	list_group_id := ARRAY(SELECT DISTINCT parent_id
							FROM public.v_group_group_node
							WHERE group_id = related_group_id);
	
	RETURN list_group_id;
END;$$;


CREATE FUNCTION get_groups_to_notify_about_administrator_group(p_schema_and_table_name text, p_log_data_id integer) RETURNS integer[]
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
related_group_id integer;

BEGIN
	EXECUTE 'SELECT group_id FROM ' || p_schema_and_table_name || ' WHERE log_data_id = ' || p_log_data_id INTO related_group_id;

	list_group_id := ARRAY(SELECT DISTINCT administrator_id
							FROM public.administrator_group
							WHERE group_id = related_group_id);
	
	RETURN list_group_id;
END;$$;


CREATE FUNCTION get_member_groups_of_user(p_user_id integer) RETURNS SETOF v_group
    AS $$declare
query text;

begin
	if p_user_id > 0 then
		query := 'SELECT * FROM public.v_group a WHERE a.group_id IN (SELECT DISTINCT b.group_id FROM public.v_user_group_node b WHERE b.member_id = ' || p_user_id || ') ORDER BY label;';

		return query execute query;
	end if;
END;
$$ LANGUAGE plpgsql;


CREATE FUNCTION get_direct_and_indirect_users_of_group(p_group_id integer) RETURNS SETOF v_user
    AS $$declare
query text;

begin
	if(p_group_id > 0) then
		query := 'SELECT DISTINCT ON (f.last_name, f.first_name, f.user_id) * FROM (SELECT a.* FROM public.v_user a JOIN public.v_user_group_node b ON (a.user_id = b.member_id) JOIN public.v_group_group_node c ON (b.group_id = c.group_id) WHERE (b.group_id = ' || p_group_id || ') UNION SELECT d.* FROM public.v_user d JOIN public.v_user_group_node e ON (d.user_id = e.member_id) WHERE (e.group_id = ' || p_group_id || ')) f ORDER BY f.last_name, f.first_name, f.user_id;';

		return query execute query;
	end if;
END;
$$ LANGUAGE plpgsql;


CREATE FUNCTION split_group(p_nb_new_children integer, p_nb_user_by_new_child integer, p_group_id integer, p_user_id integer, p_postfix text) RETURNS SETOF v_group
    AS $$DECLARE
source_group v_group%rowtype; 
list_user_members integer[];
nb_user_members real;
nb_new_children real := p_nb_new_children;
nb_user_by_new_child real := p_nb_user_by_new_child;
random_user_member_id integer;
i integer := 0;
new_group_label text;
new_group_id integer;
list_new_group_id integer[];

BEGIN
	if(p_group_id > 0) then
		SELECT array_agg(a.user_id) INTO list_user_members FROM public.get_direct_and_indirect_users_of_group(p_group_id) a;
		nb_user_members := array_length(list_user_members, 1);

		if(nb_user_members > 0) then
			SELECT * INTO source_group FROM public.v_group WHERE group_id = p_group_id;
			
			if(nb_new_children > 0) then
				if(nb_new_children > nb_user_members) then
					nb_new_children := nb_user_members;
				end if;
			elsif(nb_user_by_new_child > 0) then
				nb_new_children := ceil(nb_user_members / nb_user_by_new_child);
			else
				return;
			end if;
			
			if(nb_new_children > 0) then
				for i in 1..nb_new_children loop
					new_group_label := source_group.label || '-' || p_postfix;
					if i < 10 then
						new_group_label := new_group_label || '0';
					end if;
					new_group_label := new_group_label || i::text;

					new_group_id := nextval('public.groups_group_id_seq');
					
					INSERT INTO public.v_group (group_id, label, description, unique_label, user_id) VALUES (new_group_id, new_group_label, source_group.description, false, p_user_id);
					
					list_new_group_id := list_new_group_id || new_group_id;
				end loop;
				
				i := 1;
				while(array_length(list_user_members, 1) > 0) loop
					random_user_member_id := list_user_members[trunc(random()*array_length(list_user_members, 1)) + 1];
					list_user_members := array_remove(list_user_members, random_user_member_id);
					INSERT INTO public.v_user_group (group_id, member_id, user_id) VALUES (list_new_group_id[i], random_user_member_id, p_user_id);
					i := i % array_length(list_new_group_id, 1) + 1;
				end loop;
			end if;
		end if;
		
		return query SELECT * FROM public.v_group WHERE group_id = ANY(list_new_group_id);
	end if;
END;$$ LANGUAGE plpgsql;


SET search_path = content, pg_catalog;


CREATE RULE v_type_delete AS
    ON DELETE TO v_type DO INSTEAD  DELETE FROM content.type
  WHERE (type.type_id = old.type_id);


CREATE RULE v_type_insert AS
    ON INSERT TO v_type DO INSTEAD  INSERT INTO content.type (type_id, table_schema, table_name, application_id, url, function_name_to_notify_groups_about_an_event)
  VALUES (new.type_id, new.table_schema, new.table_name, new.application_id, new.url, new.function_name_to_notify_groups_about_an_event);


CREATE RULE v_type_update AS
    ON UPDATE TO v_type DO INSTEAD  UPDATE content.type SET table_schema = new.table_schema, table_name = new.table_name, application_id = new.application_id, url = new.url, function_name_to_notify_groups_about_an_event = new.function_name_to_notify_groups_about_an_event
  WHERE (type.type_id = old.type_id);



SET search_path = file, pg_catalog;


CREATE RULE v_corrected_file_delete AS
    ON DELETE TO v_corrected_file DO INSTEAD  DELETE FROM corrected_file
  WHERE (corrected_file.file_id = old.file_id);


CREATE RULE v_corrected_file_insert AS
    ON INSERT TO v_corrected_file DO INSTEAD  INSERT INTO corrected_file (corrected_file_id, file_id, correction_completed, user_id)
  VALUES (new.corrected_file_id, new.file_id, new.correction_completed, new.user_id);


CREATE RULE v_corrected_file_update AS
    ON UPDATE TO v_corrected_file DO INSTEAD UPDATE corrected_file SET correction_completed = new.correction_completed, user_id = new.user_id
  WHERE (corrected_file.file_id = new.file_id); 


CREATE RULE v_file_delete AS
    ON DELETE TO v_file DO INSTEAD  DELETE FROM file
  WHERE (file.file_id = old.file_id);


CREATE RULE v_file_insert AS
    ON INSERT TO v_file DO INSTEAD  INSERT INTO file (file_id, label, user_id)
  VALUES (new.file_id, new.label, new.user_id);


CREATE RULE v_file_update AS
    ON UPDATE TO v_file DO INSTEAD UPDATE file SET label = new.label
  WHERE (file.file_id = new.file_id); 


CREATE RULE v_file_group_delete AS
    ON DELETE TO v_file_group DO INSTEAD  DELETE FROM file.file_group
  WHERE (file_group.file_id = old.file_id) AND (file_group.group_id = old.group_id);


CREATE RULE v_file_group_insert AS
    ON INSERT TO v_file_group DO INSTEAD  INSERT INTO file_group (file_id, group_id, can_edit, user_id, start_valid)
  VALUES (new.file_id, new.group_id, new.can_edit, new.user_id, COALESCE(new.start_valid, now()));


CREATE RULE v_file_group_update AS
    ON UPDATE TO v_file_group DO INSTEAD  UPDATE file_group SET can_edit = new.can_edit, start_valid = COALESCE(new.start_valid, now())
  WHERE file_group.file_id = new.file_id AND file_group.group_id = new.group_id;


CREATE RULE v_folder_delete AS
    ON DELETE TO v_folder DO INSTEAD  DELETE FROM folder
  WHERE (folder.folder_id = old.folder_id);


CREATE RULE v_folder_insert AS
    ON INSERT TO v_folder DO INSTEAD  INSERT INTO folder (folder_id, parent_id, label, user_id)
  VALUES (new.folder_id, new.parent_id, new.label, new.user_id);


CREATE RULE v_folder_update AS
    ON UPDATE TO v_folder DO INSTEAD UPDATE folder SET label = new.label, parent_id = new.parent_id
  WHERE (folder.folder_id = new.folder_id); 


CREATE RULE v_folder_file_delete AS
    ON DELETE TO v_folder_file DO INSTEAD  DELETE FROM folder_file
  WHERE (folder_file.folder_id = old.folder_id) AND (folder_file.file_id = old.file_id);


CREATE RULE v_folder_file_insert AS
    ON INSERT TO v_folder_file DO INSTEAD  INSERT INTO folder_file (folder_id, file_id, user_id)
  VALUES (new.folder_id, new.file_id, new.user_id);


CREATE RULE v_folder_file_update AS
    ON UPDATE TO v_folder_file DO INSTEAD  NOTHING;


CREATE RULE v_folder_group_delete AS
    ON DELETE TO v_folder_group DO INSTEAD  DELETE FROM folder_group
  WHERE (folder_group.folder_id = old.folder_id) AND (folder_group.group_id = old.group_id);


CREATE RULE v_folder_group_insert AS
    ON INSERT TO v_folder_group DO INSTEAD  INSERT INTO folder_group (folder_id, group_id, can_edit, user_id, start_valid)
  VALUES (new.folder_id, new.group_id, new.can_edit, new.user_id, COALESCE(new.start_valid, now()));


CREATE RULE v_folder_group_update AS
    ON UPDATE TO v_folder_group DO INSTEAD  UPDATE folder_group SET can_edit = new.can_edit, start_valid = COALESCE(new.start_valid, now())
  WHERE (folder_group.folder_id = new.folder_id) AND (folder_group.group_id = new.group_id);


CREATE RULE v_turn_in_folder_delete AS
    ON DELETE TO v_turn_in_folder DO INSTEAD  DELETE FROM turn_in_folder
  WHERE (turn_in_folder.folder_id = old.folder_id);


CREATE RULE v_turn_in_folder_insert AS
    ON INSERT TO v_turn_in_folder DO INSTEAD  INSERT INTO turn_in_folder (folder_id, opening_time, closing_time, user_id)
  VALUES (new.folder_id, new.opening_time, new.closing_time, new.user_id);


CREATE RULE v_turn_in_folder_update AS
    ON UPDATE TO v_turn_in_folder DO INSTEAD UPDATE turn_in_folder SET opening_time = new.opening_time, closing_time = new.closing_time
  WHERE (turn_in_folder.folder_id = new.folder_id);


CREATE RULE v_version_delete AS
    ON DELETE TO v_version DO INSTEAD  DELETE FROM version
  WHERE (version.version_id = old.version_id);


CREATE RULE v_version_insert AS
    ON INSERT TO v_version DO INSTEAD  INSERT INTO version (version_id, file_id, path, description, user_id)
  VALUES (new.version_id, new.file_id, new.path, new.description, new.user_id);


CREATE RULE v_version_update AS
    ON UPDATE TO v_version DO INSTEAD UPDATE version SET description = new.description
  WHERE (version.version_id = new.version_id);
  

SET search_path = notification, pg_catalog;


CREATE RULE v_device_delete AS
    ON DELETE TO v_device DO INSTEAD  DELETE FROM device
  WHERE (device.device_id = old.device_id);


CREATE RULE v_device_insert AS
    ON INSERT TO v_device DO INSTEAD  INSERT INTO device (device_id, user_id)
  VALUES (new.device_id, new.user_id);


CREATE RULE v_device_update AS
    ON UPDATE TO v_device DO INSTEAD NOTHING;

	
CREATE RULE v_device_application_delete AS
    ON DELETE TO v_device_application DO INSTEAD  DELETE FROM device_application
  WHERE (device_application.device_id = old.device_id AND device_application.application_id = old.application_id);


CREATE RULE v_device_application_insert AS
    ON INSERT TO v_device_application DO INSTEAD  INSERT INTO device_application (device_id, application_id, user_id)
  VALUES (new.device_id, new.application_id, new.user_id);


CREATE RULE v_device_application_update AS
    ON UPDATE TO v_device_application DO INSTEAD NOTHING;

	
CREATE RULE v_device_user_notification_delete AS
    ON DELETE TO v_device_user_notification DO INSTEAD  NOTHING;


CREATE RULE v_device_user_notification_insert AS
    ON INSERT TO v_device_user_notification DO INSTEAD  NOTHING;


CREATE RULE v_device_user_notification_update AS
    ON UPDATE TO v_device_user_notification DO INSTEAD UPDATE device_user_notification SET sent = new.sent 
  WHERE (device_user_notification.device_id = new.device_id AND device_user_notification.event_id = new.event_id); 
  
  
CREATE RULE v_email_delete AS
    ON DELETE TO v_email DO INSTEAD  DELETE FROM email
  WHERE (email.device_id = old.device_id);


CREATE RULE v_email_insert AS
    ON INSERT TO v_email DO INSTEAD  INSERT INTO email (device_id, user_id, email_address)
  VALUES (new.device_id, new.user_id, new.email_address);


CREATE RULE v_email_update AS
    ON UPDATE TO v_email DO INSTEAD UPDATE email SET email_address = new.email_address
  WHERE (email.device_id = new.device_id);
	
  
CREATE RULE v_phone_delete AS
    ON DELETE TO v_phone DO INSTEAD  DELETE FROM phone
  WHERE (phone.device_id = old.device_id);


CREATE RULE v_phone_insert AS
    ON INSERT TO v_phone DO INSTEAD  INSERT INTO phone (device_id, user_id, phone_number, phone_service_provider_id)
  VALUES (new.device_id, new.user_id, new.phone_number, new.phone_service_provider_id);


CREATE RULE v_phone_update AS
    ON UPDATE TO v_phone DO INSTEAD UPDATE phone SET phone_number = new.phone_number, phone_service_provider_id = new.phone_service_provider_id 
  WHERE (phone.device_id = new.device_id);   

  
CREATE RULE v_phone_service_provider_delete AS
    ON DELETE TO v_phone_service_provider DO INSTEAD  DELETE FROM phone_service_provider
  WHERE (phone_service_provider.phone_service_provider_id = old.phone_service_provider_id);


CREATE RULE v_phone_service_provider_insert AS
    ON INSERT TO v_phone_service_provider DO INSTEAD  INSERT INTO phone_service_provider (phone_service_provider_id, email_address_domain_part, user_id)
  VALUES (new.phone_service_provider_id, new.email_address_domain_part, new.user_id);


CREATE RULE v_phone_service_provider_update AS
    ON UPDATE TO v_phone_service_provider DO INSTEAD UPDATE phone_service_provider SET email_address_domain_part = new.email_address_domain_part 
  WHERE (phone_service_provider.phone_service_provider_id = new.phone_service_provider_id);   
  

CREATE RULE v_web_gui_delete AS
    ON DELETE TO v_web_gui DO INSTEAD  DELETE FROM web_gui
  WHERE (web_gui.device_id = old.device_id);


CREATE RULE v_web_gui_insert AS
    ON INSERT TO v_web_gui DO INSTEAD  INSERT INTO web_gui (device_id, user_id)
  VALUES (new.device_id, new.user_id);


CREATE RULE v_web_gui_update AS
    ON UPDATE TO v_web_gui DO INSTEAD NOTHING;   
	

SET search_path = public, pg_catalog;


CREATE RULE v_administrator_group_delete AS
    ON DELETE TO v_administrator_group DO INSTEAD  DELETE FROM administrator_group
  WHERE ((administrator_group.administrator_id = old.administrator_id) AND (administrator_group.group_id = old.group_id));


CREATE RULE v_administrator_group_insert AS
    ON INSERT TO v_administrator_group DO INSTEAD  INSERT INTO administrator_group (group_id, administrator_id, user_id)
  VALUES (new.group_id, new.administrator_id, new.user_id);


CREATE RULE v_administrator_group_update AS
    ON UPDATE TO v_administrator_group DO INSTEAD  NOTHING;

  
CREATE RULE v_application_insert AS
    ON INSERT TO v_application DO INSTEAD  INSERT INTO application (application_id, url, label, description, icon, user_id)
  VALUES (new.application_id, new.url, new.label, new.description, new.icon, new.user_id);
  
  
CREATE RULE v_application_update AS
    ON UPDATE TO v_application DO INSTEAD  UPDATE application SET url = new.url, label = new.label, description = new.description, icon = new.icon
  WHERE (application.application_id = new.application_id);

  
CREATE RULE v_application_delete AS
    ON DELETE TO v_application DO INSTEAD  DELETE FROM application
  WHERE (application.application_id = old.application_id);


CREATE RULE v_application_privilege_delete AS
    ON DELETE TO v_application_privilege DO INSTEAD  DELETE FROM application_privilege
  WHERE application_privilege.application_id = old.application_id AND application_privilege.privilege_id = old.privilege_id ;


CREATE RULE v_application_privilege_insert AS
    ON INSERT TO v_application_privilege DO INSTEAD  INSERT INTO application_privilege (application_id, privilege_id, description, user_id)
  VALUES (new.application_id, new.privilege_id, new.description, new.user_id);  


CREATE RULE v_application_privilege_update AS
    ON UPDATE TO v_application_privilege DO INSTEAD  UPDATE application_privilege SET description = new.description
  WHERE (application_privilege.application_id = new.application_id and application_privilege.privilege_id = new.privilege_id);  
 
 
CREATE RULE v_application_privilege_group_delete AS
    ON DELETE TO v_application_privilege_group DO INSTEAD  DELETE FROM application_privilege_group
  WHERE (application_privilege_group.application_id = old.application_id AND application_privilege_group.privilege_id = old.privilege_id AND application_privilege_group.group_id = old.group_id);

  
CREATE RULE v_application_privilege_group_insert AS
    ON INSERT TO v_application_privilege_group DO INSTEAD  INSERT INTO application_privilege_group (application_id, privilege_id, group_id, user_id)
  VALUES (new.application_id, new.privilege_id, new.group_id, new.user_id);


CREATE RULE v_application_privilege_group_update AS
    ON UPDATE TO v_application_privilege_group DO INSTEAD  NOTHING;
  
  
CREATE RULE v_employee_delete AS
    ON DELETE TO v_employee DO INSTEAD  DELETE FROM employee
  WHERE (employee.user_id = old.user_id AND employee.employee_id = old.employee_id);
  
  
CREATE RULE v_employee_insert AS
    ON INSERT TO v_employee DO INSTEAD  INSERT INTO employee (employee_id, user_id, phone_number, office, occupation)
  VALUES (new.employee_id, new.user_id, new.phone_number, new.office, new.occupation);


CREATE RULE v_employee_update AS
    ON UPDATE TO v_employee DO INSTEAD  UPDATE employee SET employee_id = new.employee_id, phone_number = new.phone_number, office = new.office, occupation = new.occupation
  WHERE (employee.user_id = new.user_id);


CREATE RULE v_group_group_delete AS
    ON DELETE TO v_group_group DO INSTEAD  DELETE FROM group_group
  WHERE ((group_group.parent_id = old.parent_id) AND (group_group.group_id = old.group_id));

  
CREATE RULE v_group_group_insert AS
    ON INSERT TO v_group_group DO INSTEAD  INSERT INTO group_group (group_id, parent_id, user_id)
  VALUES (new.group_id, new.parent_id, new.user_id);


CREATE RULE v_group_group_update AS
    ON UPDATE TO v_group_group DO INSTEAD  NOTHING;


CREATE RULE v_group_delete AS
    ON DELETE TO v_group DO INSTEAD DELETE FROM groups
  WHERE (groups.group_id = old.group_id);

  
CREATE RULE v_group_insert AS
    ON INSERT TO v_group DO INSTEAD  INSERT INTO groups (group_id, user_id, label, description, last_modification, unique_label)
  VALUES (new.group_id, new.user_id, new.label, new.description, now(), new.unique_label);

  
CREATE RULE v_group_update AS
    ON UPDATE TO v_group DO INSTEAD  UPDATE groups SET label = new.label, description = new.description, last_modification = now(), unique_label = new.unique_label
  WHERE (groups.group_id = new.group_id); 
  
  
CREATE RULE v_parameter_insert AS
    ON INSERT TO v_parameter DO INSTEAD  INSERT INTO parameter (application_id, parameter_key, parameter_value, user_id)
  VALUES (new.application_id, new.parameter_key, new.parameter_value, new.user_id);
  

CREATE RULE v_parameter_update AS
    ON UPDATE TO v_parameter DO INSTEAD  UPDATE parameter SET parameter_value = new.parameter_value
  WHERE parameter.application_id = new.application_id AND parameter.parameter_key = old.parameter_key;
 

CREATE RULE v_parameter_delete AS
    ON DELETE TO v_parameter DO INSTEAD  DELETE FROM parameter
  WHERE (parameter.application_id = old.application_id and parameter.parameter_key = old.parameter_key);
 
 
CREATE RULE v_privilege_delete AS
    ON DELETE TO v_privilege DO INSTEAD  DELETE FROM privilege
  WHERE (privilege.privilege_id = old.privilege_id);
  
  
CREATE RULE v_privilege_insert AS
    ON INSERT TO v_privilege DO INSTEAD  INSERT INTO privilege (privilege_id, user_id, label, description)
  VALUES (new.privilege_id, new.user_id, new.label, new.description);


CREATE RULE v_privilege_update AS
    ON UPDATE TO v_privilege DO INSTEAD  UPDATE privilege SET label = new.label, description = new.description
  WHERE (privilege.privilege_id = new.privilege_id);


CREATE RULE v_profile_picture_delete AS
    ON DELETE TO v_profile_picture DO INSTEAD  DELETE FROM profile_picture
  WHERE (profile_picture.user_id = old.user_id);


CREATE RULE v_profile_picture_insert AS
    ON INSERT TO v_profile_picture DO INSTEAD  INSERT INTO profile_picture (user_id, user_agreement, administrator_user_id, administrator_approval, image, icon)
  VALUES (new.user_id, new.user_agreement, new.administrator_user_id, new.administrator_approval, new.image, new.icon);


CREATE RULE v_profile_picture_update AS
    ON UPDATE TO v_profile_picture DO INSTEAD  UPDATE profile_picture SET user_agreement = new.user_agreement, administrator_user_id = new.administrator_user_id, administrator_approval = new.administrator_approval, image = new.image, icon = new.icon
  WHERE (profile_picture.user_id = new.user_id);

  
CREATE RULE v_student_delete AS
    ON DELETE TO v_student DO INSTEAD  DELETE FROM student
  WHERE (student.user_id = old.user_id AND student.student_id = old.student_id);

  
CREATE RULE v_student_insert AS
    ON INSERT TO v_student DO INSTEAD  INSERT INTO student (student_id, user_id)
  VALUES (new.student_id, new.user_id);
 
 
CREATE RULE v_student_update AS
    ON UPDATE TO v_student DO INSTEAD  UPDATE student SET student_id = new.student_id
  WHERE (student.user_id = new.user_id);


CREATE OR REPLACE RULE v_user_delete AS
    ON DELETE TO v_user DO INSTEAD  UPDATE users SET valid_end = now()
  WHERE users.user_id = old.user_id;
  
  
CREATE RULE v_user_insert AS
    ON INSERT TO v_user DO INSTEAD  INSERT INTO users (user_id, administrative_user_id, first_name, last_name, email_address, gender)  
		SELECT new.user_id, new.administrative_user_id, new.first_name, new.last_name, new.email_address, new.gender
          WHERE NOT (EXISTS ( SELECT users.user_id
                   FROM users
                  WHERE users.user_id = new.user_id));


CREATE RULE v_user_update AS
    ON UPDATE TO v_user DO INSTEAD  UPDATE users SET administrative_user_id = new.administrative_user_id, first_name = new.first_name, last_name = new.last_name, email_address = new.email_address, gender = new.gender, valid_end = new.valid_end
  WHERE users.user_id = new.user_id;


CREATE RULE v_user_group_delete AS
    ON DELETE TO v_user_group DO INSTEAD  DELETE FROM user_group
  WHERE ((user_group.group_id = old.group_id) AND (user_group.member_id = old.member_id));


CREATE RULE v_user_group_insert AS
    ON INSERT TO v_user_group DO INSTEAD  INSERT INTO user_group (group_id, member_id, user_id)  SELECT new.group_id,
            new.member_id, new.user_id
          WHERE (NOT EXISTS ( SELECT user_group.group_id,
                    user_group.member_id
                   FROM user_group
                  WHERE ((user_group.group_id = new.group_id) AND (user_group.member_id = new.member_id))));


CREATE RULE v_user_group_update AS
    ON UPDATE TO v_user_group DO INSTEAD NOTHING;
	
-- ********************************* TRIGGER FUNCTIONS ********************************* --

SET search_path = audit, pg_catalog;


CREATE FUNCTION event_trig_funct() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
function_name text;
g_id integer;
u_id integer;
list_user_column_name text[][2];
user_column_name text[2];
list_tmp_users_id_to_notify integer[];
tmp_user_id_to_notify integer;
list_users_to_notify integer[];

BEGIN
IF(NEW.log_data_id IS NOT NULL) THEN
	SELECT function_name_to_notify_groups_about_an_event INTO function_name FROM content.type WHERE table_schema = NEW.table_schema AND table_name = new.table_name;

	IF(function_name IS NOT NULL) THEN
		EXECUTE 'SELECT * FROM ' || function_name || '(' || quote_literal(NEW.table_schema || '.' || NEW.table_name) || ', ' || NEW.log_data_id || ')' INTO list_group_id;
		
		IF(array_length(list_group_id, 1) > 0) THEN
			FOR g_id IN (SELECT unnest(list_group_id)) LOOP
				SELECT array_agg(user_id) INTO list_tmp_users_id_to_notify FROM public.get_direct_and_indirect_users_of_group(g_id);
				list_users_to_notify := list_users_to_notify || list_tmp_users_id_to_notify;
			END LOOP;
		END IF;
	ELSE
		SELECT ARRAY[tc.table_schema || '.' || tc.table_name, kcu.column_name]
			INTO list_user_column_name
			FROM information_schema.table_constraints AS tc 
			JOIN information_schema.key_column_usage AS kcu
			  ON tc.constraint_name = kcu.constraint_name
			JOIN information_schema.constraint_column_usage AS ccu
			  ON ccu.constraint_name = tc.constraint_name
			WHERE constraint_type = 'FOREIGN KEY' AND tc.table_schema = new.table_schema AND tc.table_name = new.table_name AND ccu.table_schema = 'public' AND ccu.table_name = 'users' AND ccu.column_name = 'user_id';
		
		IF(array_length(list_user_column_name, 1) > 0) THEN	
			FOREACH user_column_name SLICE 1 IN ARRAY list_user_column_name LOOP
				EXECUTE 'SELECT ' || user_column_name[2] || ' FROM ' || user_column_name[1] || ' WHERE log_data_id = ' || NEW.log_data_id INTO tmp_user_id_to_notify;
				list_users_to_notify := list_users_to_notify || tmp_user_id_to_notify;
			END LOOP;
		END IF;
	END IF;

	IF(array_length(list_users_to_notify, 1) > 0) THEN
		FOR u_id IN (SELECT unnest(list_users_to_notify)) LOOP
			IF(NEW.user_id <> u_id) THEN
				PERFORM notification.notify_user(NEW.event_id, u_id);
			END IF;
		END LOOP;

		PERFORM pg_notify('notification', NEW.event_id::text);
	END IF;
END IF;

RETURN NULL; -- result is ignored since this is an AFTER trigger

END;$$;


SET search_path = file, pg_catalog;


CREATE FUNCTION folder_check_no_cycle_trig_funct() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
	
BEGIN

IF NEW.folder_id <> NEW.parent_id THEN
	IF NOT EXISTS(SELECT 1 FROM file.v_folder WHERE node_id = NEW.folder_id AND folder_id = NEW.parent_id) THEN
	  RETURN NEW;
	ELSE
	  RETURN NULL;
	END IF;
ELSE
	RETURN NULL;
END IF;

END;$$;


SET search_path = public, pg_catalog;


CREATE FUNCTION group_group_check_no_cycle_trig_funct() RETURNS trigger
    LANGUAGE plpgsql
    AS $$DECLARE

list_group_id integer[];
	
BEGIN

IF NEW.group_id <> NEW.parent_id THEN
	IF NOT EXISTS(SELECT 1 FROM public.v_group_group_node WHERE node_id = NEW.group_id AND group_id = NEW.parent_id) THEN
	  RETURN NEW;
	ELSE
	  RETURN NULL;
	END IF;
ELSE
	RETURN NULL;
END IF;

END;$$;


-- ********************************* TRIGGERS ********************************* --


SET search_path = audit, pg_catalog;


CREATE TRIGGER event_trig AFTER INSERT ON event FOR EACH ROW EXECUTE PROCEDURE event_trig_funct();


SET search_path = file, pg_catalog;


CREATE TRIGGER folder_check_no_cycle_trig BEFORE INSERT OR UPDATE ON folder FOR EACH ROW EXECUTE PROCEDURE folder_check_no_cycle_trig_funct();


SET search_path = public, pg_catalog;


CREATE TRIGGER group_group_check_no_cycle_trig BEFORE INSERT OR UPDATE ON group_group FOR EACH ROW EXECUTE PROCEDURE group_group_check_no_cycle_trig_funct();


SET search_path = audit, pg_catalog;


SELECT audit_table('file.corrected_file', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('file.file', 'true', 'true', '{log_data_id, file_id, user_id, registration}'::text[]);

SELECT audit_table('file.file_group', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('file.folder', 'true', 'true', '{log_data_id, folder_id, user_id, registration}'::text[]);

SELECT audit_table('file.folder_file', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('file.folder_group', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('file.turn_in_folder', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('file.version', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);


SELECT audit_table('public.administrator_group', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('public.application', 'true', 'true', '{log_data_id, application_id, user_id, registration}'::text[]);

SELECT audit_table('public.application_privilege', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('public.application_privilege_group', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

SELECT audit_table('public.groups', 'true', 'true', '{log_data_id, group_id, user_id, last_modification, registration}'::text[]);

SELECT audit_table('public.user_group', 'true', 'true', '{log_data_id, user_id, registration}'::text[]);

   TRUNCATE public.users RESTART IDENTITY CASCADE;


   
-- Add Dummy Data
-- SCHEMA PUBLIC
set search_path to public;


-- USER

INSERT INTO users (administrative_user_id, last_name, first_name, email_address) VALUES ('supa1234', 'Super', 'Admin', 'Admin.Super@USherbrooke.ca'); -- TODO : veuillez ajouter autant d'usagers que le nombre de membres de votre équipe afin que chacun puisse s'authentifier avec ses informations personnelles auprès de votre application GWT

ALTER SEQUENCE public.users_user_id_seq RESTART WITH 2; -- TODO : remplacer 2 par la valeur à laquelle la séquence doit repartir


-- APPLICATION

INSERT INTO application (label, url, description, user_id) VALUES ('Mon application', 'localhost:8888/monApplication', 'Ceci est la description de mon application.', 1); -- TODO : veuillez renommer les champs pour qu'ils correspondent à votre application

ALTER SEQUENCE public.application_application_id_seq RESTART WITH 2;


-- PRIVILEGE

INSERT INTO privilege (label, description, user_id) VALUES ('Accès membre', 'Accès membre à l''application', 1);

ALTER SEQUENCE public.privilege_privilege_id_seq RESTART WITH 2;


-- GROUP

INSERT INTO groups (label, description, unique_label, user_id) VALUES ('Administrateurs Opus', 'Groupe des administrateurs du système Opus', false, 1);

ALTER SEQUENCE public.groups_group_id_seq RESTART WITH 2;


-- ADMINISTRATOR_GROUP

INSERT INTO administrator_group (group_id, administrator_id, user_id) VALUES (1, 1, 1);


-- USER_GROUP

INSERT INTO user_group (member_id, group_id, user_id) VALUES (1, 1, 1); -- TODO : veuillez ajouter une entrée pour chaque membre de votre équipe désirant avoir accès à l'application OpusProjectModel lors de son exécution


-- APPLICATION_PRIVILEGE

INSERT INTO application_privilege (application_id, privilege_id, user_id) VALUES (1, 1, 1);


-- APPLICATION_PRIVILEGE_GROUP

INSERT INTO application_privilege_group (application_id, privilege_id, group_id, user_id) VALUES (1, 1, 1, 1);


-- EMPLOYEE

INSERT INTO employee (employee_id, user_id, phone_number, office, occupation) VALUES ('04000001', 1, '819-821-8000 #1234', 'C1-3111', 'Coordonnateur');



-- STUFF FOR SEARCHUS search team
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

INSERT INTO file.File (label, user_id)
VALUES
('TestFile', 1),
('TestFileAdmin', 1);

-- Add edit and view permissions on all files for admins
INSERT INTO file.File_Group (file_id, group_id, can_edit, user_id)
SELECT f.file_id, g.group_id, 'true', 1 
FROM file.file f
CROSS JOIN groups g
WHERE g.label = 'Administrateurs Opus';

-- Add view permissions on TestFile for Students
INSERT INTO file.File_Group (file_id, group_id, can_edit, user_id)
SELECT f.file_id, g.group_id, 'false', 1 
FROM file.file f
CROSS JOIN groups g
WHERE g.label = 'Etudiants' AND f.label = 'TestFile';

INSERT INTO file.version(file_id, description, path, user_id)
SELECT F.file_id , 'First version of the file', 'C:\temp\'||f.label, 1
FROM file.file F;

INSERT INTO file.version(file_id, description, path, user_id)
SELECT F.file_id , 'Second version of the file', 'C:\temp\'||f.label||'v2', 1
FROM file.file F;

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


  -- Assert that function works
  SELECT * FROM file.get_CurrentVersionFiles('babm2002');


  
