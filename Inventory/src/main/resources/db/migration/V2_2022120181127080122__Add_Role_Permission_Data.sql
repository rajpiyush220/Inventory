drop procedure if exists `?`;
delimiter //
create procedure `?`()
begin
    declare continue handler for sqlexception begin
    end;

        -- Product category permissions

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_VIEW','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_UPDATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_DELETE','ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_CREATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_VIEW','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_UPDATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_DELETE','MANAGER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CAT_VIEW','SUPERVISOR');

        -- User permissions
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'ADMIN_CREATE','SUPER_ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'MANAGER_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SUPERVISOR_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'USER_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STAFF_CREATE','ADMIN');

        -- Product permission
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_VIEW','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_UPDATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_DELETE','ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CREATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_VIEW','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_UPDATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_DELETE','MANAGER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_VIEW','SUPERVISOR');

        -- Stock permission
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_VIEW','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_UPDATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_DELETE','ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_CREATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_VIEW','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_UPDATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_DELETE','MANAGER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_VIEW','SUPERVISOR');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_VIEW','USER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_VIEW','STAFF');

        -- Stock audit permission
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','MANAGER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','SUPERVISOR');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','USER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','STAFF');

        -- Insert sales permission
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_VIEW','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_UPDATE','ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_CREATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_VIEW','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_UPDATE','MANAGER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_CREATE','SUPERVISOR');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_VIEW','SUPERVISOR');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_UPDATE','SUPERVISOR');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_CREATE','USER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_VIEW','USER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_UPDATE','USER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_CREATE','STAFF');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_VIEW','STAFF');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SALES_UPDATE','STAFF');

end //
delimiter ;
call `?`();
drop procedure if exists `?`;


