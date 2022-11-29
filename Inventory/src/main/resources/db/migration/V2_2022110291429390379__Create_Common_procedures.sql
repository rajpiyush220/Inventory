-- Create procedure AddColumnIfNotExists
DELIMITER ;;
CREATE  PROCEDURE `AddColumnIfNotExists`(
	given_database VARCHAR(128),
    given_table VARCHAR(128),
    given_column VARCHAR(128),
    given_column_description VARCHAR(128))
BEGIN
	DECLARE ColumnIsThere INTEGER;
    select count(1) into ColumnIsThere
    from information_schema.columns
    where table_schema = given_database
      and TABLE_NAME = given_table
      and COLUMN_NAME = given_column;
	IF ColumnIsThere = 0 THEN
        SET @sqlstmt = CONCAT('alter table ', given_table, ' add column ', given_column, ' ',
                              given_column_description);
        PREPARE st FROM @sqlstmt;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    ELSE
        SELECT CONCAT('Index ', given_column, ' already exists on table ',
                      given_database, '.', given_table) CreateColumnInfoMessage;
    END IF;

END ;;
DELIMITER ;
-- Create procedure AddColumnIfNotExistsAfterColumn
DELIMITER ;;
CREATE  PROCEDURE `AddColumnIfNotExistsAfterColumn`(
	given_database varchar(128),
    given_table varchar(128),
    given_column varchar(128),
    given_column_description varchar(128),
    after_column varchar(128))
BEGIN
	declare ColumnisThere integer;
    select count(1)
    into ColumnIsThere
    from information_schema.columns
    where table_schema = given_database
      and table_name = given_table
      and column_name = given_column;
    if ColumnIsThere = 0 then
        set @sqlstmt = concat('alter table ', given_table,
                              ' add column ', given_column, ' ', given_column_description,
                              ' after ', after_column);
        prepare st from @sqlstmt;
        executE st;
        deallocate prepare st;
    else
        select concat('Column ', given_column, ' already exists on table ',
                      given_database, '.', given_table) CreateColumnInfoMessage;
    end if;
END ;;
DELIMITER ;
-- Create procedure AddForeignKeyIfNotExists
DELIMITER ;;
CREATE  PROCEDURE `AddForeignKeyIfNotExists`(
	given_database VARCHAR(128),
    given_table VARCHAR(128),
    given_column VARCHAR(128),
    given_references_table VARCHAR(128),
    given_references_column varchar(128))
BEGIN
	DECLARE KeyIsThere INTEGER;
    select count(1)
    into KeyIsThere
    FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = given_database
      AND TABLE_NAME = given_table
      AND COLUMN_NAME = given_column
      and REFERENCED_TABLE_NAME = given_references_table
      and REFERENCED_COLUMN_NAME = given_references_column;
    IF KeyIsThere = 0 THEN
        SET @sqlstmt = CONCAT('alter table ', given_table,
                              ' add foreign key (', given_column, ') ',
                              ' references ', given_references_table, ' (', given_references_column,
                              ')');
        PREPARE st FROM @sqlstmt;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    ELSE
        SELECT CONCAT('Key on ', given_column, ' already exists on table ',
                      given_database, '.', given_table) CreateKeyInfoMessage;
    END IF;

END ;;
DELIMITER ;
-- Create procedure AddNamedForeignKeyIfNotExists
DELIMITER ;;
CREATE  PROCEDURE `AddNamedForeignKeyIfNotExists`(
	foreign_key_name varchar(128),
    table_name varchar(128),
    column_names varchar(255),
    ref_table_name varchar(128),
    ref_column_names varchar(255))
BEGIN
	if not exists(
            select true
            from information_schema.table_constraints
            where constraint_schema = database()
              and table_name = table_name
              and constraint_name = foreign_key_name) then

        set @var = concat(
                'alter table ', table_name,
                ' add constraint ', foreign_key_name,
                ' foreign key (', column_names, ')',
                ' references ', ref_table_name, '(', ref_column_names, ')');

        prepare statement from @var;
        execute statement;
        deallocate prepare statement;

    end if;
END ;;
DELIMITER ;
-- Create procedure AlterColumnIfExists
DELIMITER ;;
CREATE  PROCEDURE `AlterColumnIfExists`(
	given_database varchar(128),
   given_table varchar(128),
   given_column_name varchar(128),
   given_column_type varchar(128))
BEGIN
	declare present integer;
    select count(1)
    into present
    from information_schema.columns
    where table_schema = given_database
      and table_name = given_table
      and column_name = given_column_name;

    if present = 1 then
        set @sqlstmt =
                CONCAT('alter table ', given_table, ' modify ', given_column_name, ' ',
                       given_column_type);
        prepare st from @sqlstmt;
        execute st;
        deallocate prepare st;

        select concat('Column ', given_column_name, ' altered on table ',
                      given_database, '.', given_table) CreateColumnInfoMessage;
    else
        select concat('Column ', given_column_name, ' not present on table ',
                      given_database, '.', given_table) CreateColumnInfoMessage;
    end if;

END ;;
DELIMITER ;
-- Create procedure BatchUpdate
DELIMITER ;;
CREATE  PROCEDURE `BatchUpdate`(update_statement mediumtext)
BEGIN
	set @x = update_statement;
    prepare upd from @x;
    updateLoop:
    loop
        execute upd;
        if row_count() = 0 then
            select concat(row_count(), ' rows updated');
            leave updateLoop;
        end if;
        select concat(row_count(), ' rows updated');
    end loop;
    deallocate prepare upd;
END ;;
DELIMITER ;
-- Create procedure CreateConstraint
DELIMITER ;;
CREATE  PROCEDURE `CreateConstraint`(
	IN given_database varchar(64),
	IN given_table varchar(64),
	IN given_constraint varchar(64),
	IN given_columns varchar(255),
	IN given_unique tinyint(1))
BEGIN
	DECLARE ConstraintIsThere INTEGER;

    SELECT COUNT(1)
    INTO ConstraintIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = given_database
      AND table_name = given_table
      AND index_name = given_constraint;

    IF ConstraintIsThere = 0 THEN
        IF given_unique = true THEN
            SET @sqlstmt =
                    CONCAT('ALTER TABLE ', given_database, '.', given_table, ' ADD CONSTRAINT ',
                           given_constraint, ' UNIQUE (', given_columns, ')');
        ELSE
            SET @sqlstmt =
                    CONCAT('ALTER TABLE ', given_database, '.', given_table, ' ADD CONSTRAINT ',
                           given_constraint, ' (', given_columns, ')');
        END if;

        PREPARE st FROM @sqlstmt;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    ELSE
        SELECT CONCAT('Constraint ', given_constraint, ' already exists on Table ',
                      given_database, '.', given_table) CreateindexErrorMessage;
    END IF;

END ;;
DELIMITER ;
-- Create procedure CreateIndex
DELIMITER ;;
CREATE  PROCEDURE `CreateIndex`(
	given_database VARCHAR(64),
    given_table VARCHAR(64),
    given_index VARCHAR(64),
    given_columns VARCHAR(64))
BEGIN
	DECLARE IndexIsThere INTEGER;

    SELECT COUNT(1)
    INTO IndexIsThere
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE table_schema = given_database
      AND table_name = given_table
      AND index_name = given_index;

    IF IndexIsThere = 0 THEN
        SET @sqlstmt = CONCAT('CREATE INDEX ', given_index, ' ON ',
                              given_database, '.', given_table, ' (', given_columns, ')');
        PREPARE st FROM @sqlstmt;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    ELSE
        SELECT CONCAT('Index ', given_index, ' already exists on Table ',
                      given_database, '.', given_table) CreateindexErrorMessage;
    END IF;

END ;;
DELIMITER ;
-- Create procedure DropColumnIfExists
DELIMITER ;;
CREATE  PROCEDURE `DropColumnIfExists`(
	given_database VARCHAR(128),
    given_table VARCHAR(128),
    given_column VARCHAR(128))
BEGIN
	DECLARE ColumnIsThere INTEGER;
    DECLARE ForeignKeyIsThere INTEGER;
    DECLARE ForeignKey VARCHAR(128);
    select count(1)
    into ColumnIsThere
    from information_schema.columns
    where table_schema = given_database
      and TABLE_NAME = given_table
      and COLUMN_NAME = given_column;

    IF ColumnIsThere = 1 THEN

        select count(1)
        into ForeignKeyIsThere
        FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
        WHERE TABLE_SCHEMA = given_database
          AND TABLE_NAME = given_table
          AND COLUMN_NAME = given_column;
        if ForeignKeyIsThere = 1 THEN
            -- first remove foreign keys
            SELECT CONSTRAINT_NAME
            INTO
                ForeignKey
            FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
            WHERE TABLE_SCHEMA = given_database
              AND TABLE_NAME = given_table
              AND COLUMN_NAME = given_column
            LIMIT 1;
            -- prevent case of list

            -- drop key where name in list
            SET @sqlstmt = CONCAT('alter table ', given_table, ' drop foreign key ', ForeignKey);
            PREPARE st FROM @sqlstmt;
            EXECUTE st;
            DEALLOCATE PREPARE st;
        END IF;

        -- now drop column
        SET @sqlstmt = CONCAT('alter table ', given_table, ' drop column ', given_column);
        PREPARE st FROM @sqlstmt;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    END IF;

END ;;
DELIMITER ;
-- Create procedure DropForeignKeyIfExists
DELIMITER ;;
CREATE  PROCEDURE `DropForeignKeyIfExists`(
	foreign_key_name varchar(128),
    table_name varchar(128))
BEGIN
	if exists(
            select true
            from information_schema.table_constraints
            where constraint_schema = database()
              and table_name = table_name
              and constraint_name = foreign_key_name
              and constraint_type = 'FOREIGN KEY') then

        set @var = concat('alter table ', table_name, ' drop foreign key ', foreign_key_name);
        prepare statement from @var;
        execute statement;
        deallocate prepare statement;

    end if;

END ;;
DELIMITER ;
-- Create procedure DropIndexIfExists
DELIMITER ;;
CREATE  PROCEDURE `DropIndexIfExists`(
	target_table_name varchar(128),
    target_index_name varchar(128))
BEGIN
	if exists(
            select true
            from information_schema.statistics
            where table_name = target_table_name
              and index_name = target_index_name) then

        set @var = concat('drop index ', target_index_name, ' on ', target_table_name);

        prepare statement from @var;
        execute statement;
        deallocate prepare statement;

    end if;
END ;;
DELIMITER ;
-- Create procedure RenameColumnIfExists
DELIMITER ;;
CREATE  PROCEDURE `RenameColumnIfExists`(
	given_database varchar(128),
    given_table varchar(128),
    given_old_column varchar(128),
    given_new_column varchar(128))
BEGIN
	declare present integer;
    select count(1)
    into present
    from information_schema.columns
    where table_schema = given_database
      and table_name = given_table
      and column_name = given_old_column;

    if present = 1 then
        set @sqlstmt =
                CONCAT('alter table ', given_table, ' rename column ', given_old_column, ' to ',
                       given_new_column);
        prepare st from @sqlstmt;
        execute st;
        deallocate prepare st;
    end if;

END ;;
DELIMITER ;
-- Create procedure RenameTableIfExists
DELIMITER ;;
CREATE PROCEDURE `RenameTableIfExists`(
	given_database VARCHAR(128),
    source_table_name VARCHAR(128),
    target_table_name VARCHAR(128))
BEGIN
	DECLARE TableIsThere INTEGER;
    select count(1)
    into TableIsThere
    from information_schema.tables
    where table_schema = given_database
      and TABLE_NAME = source_table_name;

    IF TableIsThere = 1 THEN
        SET @sqlstmt = CONCAT('RENAME table ', source_table_name, ' TO ', target_table_name);
        PREPARE st FROM @sqlstmt;
        EXECUTE st;
        DEALLOCATE PREPARE st;
    ELSE
        SELECT CONCAT('Table ', source_table_name, ' not exists on database ', given_database)
                   RenameTableInfoMessage;
    END IF;

END ;;
DELIMITER ;


