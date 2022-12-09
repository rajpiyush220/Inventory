drop procedure if exists `?`;
delimiter //
create procedure `?`()
begin
    declare continue handler for sqlexception begin
    end;
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_VIEW','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_UPDATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_DELETE','ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_CREATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_VIEW','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_UPDATE','MANAGER');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_DELETE','MANAGER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'PROD_VIEW','SUPERVISOR');

end //
delimiter ;
call `?`();
drop procedure if exists `?`;


