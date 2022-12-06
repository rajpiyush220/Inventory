drop procedure if exists `?`;
delimiter //
create procedure `?`()
begin
    declare continue handler for sqlexception begin
    end;

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'ADMIN_CREATE','SUPER_ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'MANAGER_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'SUPERVISOR_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'USER_CREATE','ADMIN');
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STAFF_CREATE','ADMIN');

end //
delimiter ;
call `?`();
drop procedure if exists `?`;


