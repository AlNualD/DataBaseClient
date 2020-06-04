-- Database: LAB4_template

-- DROP DATABASE "LAB4_template";


CREATE DATABASE "LAB4_template"
    WITH 
    OWNER = user_for_lab4
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;
	
--------------------------------------------	
	
CREATE TABLE PLAYERS (
id_code char(5) UNIQUE NOT NULL,
pname char(20) NOT NULL,
characters_count int default 0 check(characters_count >= 0),
status char(30),
PRIMARY KEY (id_code)
);	

CREATE TABLE PCHARACTERS (
id_code char(5) UNIQUE NOT NULL, 
owner_code char(5) NOT NULL,
c_class char(15) NOT NULL,
c_name char(20) NOT NULL,
lvl int default 1 check(lvl > 0),
PRIMARY KEY (id_code)	
);

CREATE INDEX characters_classes_index on PCHARACTERS(c_class);



CREATE OR REPLACE  FUNCTION update_amount() RETURNS trigger AS $$
	DECLARE 
		owner_id char(5);
				modif int;
	BEGIN
	modif:= to_number(TG_ARGV[0], 'S9');
	if modif > 0 then 
	owner_id := NEW.owner_code;
	else owner_id := OLD.owner_code;
	end if;
				
				--modif:= cast(TG_ARGV[0] as int);
		EXECUTE	'UPDATE PLAYERS SET characters_count = characters_count+($1)  WHERE id_code = $2' USING modif, owner_id;
		RETURN NEW;
	END
$$ LANGUAGE 'plpgsql';


CREATE TRIGGER inc_amount_trigger 
AFTER INSERT ON  PCHARACTERS
FOR EACH ROW
EXECUTE FUNCTION update_amount('+1');

CREATE TRIGGER dec_amount_trigger
AFTER DELETE ON PCHARACTERS
FOR EACH ROW
EXECUTE FUNCTION update_amount('-1');

----------------------------------------------
CREATE FUNCTION add_player(p_name char(20), p_id char(5)) RETURNS void AS $$
INSERT INTO PLAYERS (id_code, pname)
VALUES (p_id,p_name);
$$ LANGUAGE sql;

CREATE FUNCTION add_character(c_id char(5), p_id char(5), cname char(20), cclass char(15)) RETURNS void AS $$
INSERT INTO PCHARACTERS (id_code, owner_code, c_class, c_name)
VALUES (c_id,p_id,cclass,cname);
$$ LANGUAGE sql;
-------------------------------------------------

CREATE FUNCTION add_status (p_id char(5), p_status char(30)) RETURNS void AS $$
UPDATE PLAYERS SET status = p_status WHERE PLAYERS.id_code = p_id;
$$ LANGUAGE sql;

CREATE FUNCTION update_player_inf(p_id char(5), p_name char(20), p_status char(30)) RETURNS void AS $$
UPDATE PLAYERS SET pname = p_name, status = p_status WHERE PLAYERS.id_code = p_id; 
$$  LANGUAGE sql;

--


CREATE FUNCTION update_character_inf(c_id char(5), newclass char(15), newname char(20), newlvl int) RETURNS void AS $$
UPDATE PCHARACTERS SET c_name = newname, c_class = newclass, lvl = newlvl WHERE PCHARACTERS.id_code = c_id; 
$$ LANGUAGE sql; 
---------------------------------------------------------
CREATE FUNCTION clear_table_PCCARACTERS() RETURNS void AS $$
DELETE FROM PCHARACTERS;
$$ LANGUAGE sql;
----
CREATE FUNCTION clear_tables() RETURNS void AS $$
select clear_table_PCCARACTERS();
DELETE FROM PLAYERS; 
$$ LANGUAGE sql;
----------------------------------------------------------

CREATE OR REPLACE FUNCTION find_class(f_class char(15)) RETURNS TABLE(id_code char(5), owner_id char(5), c_class char(15), c_name char(20), lvl int) AS $$
SELECT * FROM PCHARACTERS
WHERE c_class = f_class;
$$ LANGUAGE sql;

CREATE FUNCTION find_c_name(f_name char(20)) RETURNS TABLE(id_code char(5), owner_id char(5), c_class char(15), c_name char(20), lvl int) AS $$
SELECT * FROM PCHARACTERS
WHERE c_name = f_name;
$$ LANGUAGE sql;

CREATE FUNCTION find_p_name(f_name char(20)) RETURNS TABLE(id_code char(5), pname char(20), characters_count int, status char(30)) AS $$
SELECT * FROM PLAYERS WHERE pname = f_name;
$$ LANGUAGE sql;


CREATE FUNCTION find_amount (amount int) RETURNS TABLE(id_code char(5), pname char(20), characters_count int, status char(30)) AS $$
SELECT * FROM PLAYERS WHERE characters_count = amount;
$$ LANGUAGE sql;
-----------------------------------------------------------------------------------------------------------
CREATE FUNCTION delete_player(p_id char(5)) RETURNS void AS $$
DELETE FROM PCHARACTERS WHERE owner_code = p_id;
DELETE FROM PLAYERS WHERE PLAYERS.id_code = p_id;
$$ LANGUAGE sql;

CREATE FUNCTION delete_character(c_id char(5)) RETURNS void AS $$
DELETE FROM PCHARACTERS WHERE PCHARACTERS.id_code = c_id;
$$ LANGUAGE sql;

CREATE FUNCTION delete_characters_by_class(d_class char(15)) RETURNS void AS $$
DELETE FROM PCHARACTERS WHERE c_class = d_class;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION delete_players_by_amount(amount int) RETURNS void AS $$
DECLARE
 id_for_del PLAYERS%ROWTYPE;
BEGIN
if amount <> 0 then 
FOR id_for_del IN (SELECT * FROM PLAYERS WHERE characters_count = amount)
LOOP
DELETE FROM PCHARACTERS WHERE owner_code = id_for_del.id_code;
DELETE FROM PLAYERS WHERE PLAYERS.id_code = id_for_del.id_code;
end LOOP;
else 
DELETE FROM PLAYERS WHERE id_code = ANY (SELECT id_code FROM PLAYERS WHERE characters_count = amount);
end if;
END
$$ LANGUAGE 'plpgsql';

CREATE FUNCTION delete_p_by_name(d_name char(20)) RETURNS void AS $$
DECLARE 
  id_for_del PLAYERS%ROWTYPE;
 BEGIN
	FOR id_for_del IN (SELECT * FROM PLAYERS WHERE pname = d_name)
	LOOP
	DELETE FROM PCHARACTERS WHERE owner_code = id_for_del.id_code;
	DELETE FROM PLAYERS WHERE PLAYERS.id_code = id_for_del.id_code;
	end LOOP;
 END
$$ LANGUAGE 'plpgsql';

CREATE FUNCTION delete_c_by_name(d_name char(20)) RETURNS void AS $$
 DELETE FROM PCHARACTERS WHERE c_name = d_name;
$$ LANGUAGE sql;

-------------------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION show_players() RETURNS TABLE(id_code char(5), pname char(20), characters_count int, status char(30)) AS $$
BEGIN
return query select PLAYERS.id_code, PLAYERS.pname, PLAYERS.characters_count, PLAYERS.status  FROM PLAYERS; 
END
$$ LANGUAGE 'plpgsql';

CREATE OR REPLACE FUNCTION show_characters() RETURNS TABLE(id_code char(5), owner_id char(5), c_class char(15), c_name char(20), lvl int) AS $$
SELECT * FROM PCHARACTERS;
$$ LANGUAGE sql;
--------------------------------------------------------------------------------------------------

CREATE FUNCTION drop_lab4_db() RETURNS void AS $$
DROP  DATABASE IF EXISTS "LAB4";
$$ LANGUAGE sql;
-----------
CREATE FUNCTION add_status (p_id char(5), p_status char(30)) RETURNS void AS $$
UPDATE PLAYERS SET status = p_status WHERE PLAYERS.id_code = p_id;
$$ LANGUAGE sql;
