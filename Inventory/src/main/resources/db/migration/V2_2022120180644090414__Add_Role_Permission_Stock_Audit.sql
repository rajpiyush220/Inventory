drop procedure if exists `?`;
delimiter //
create procedure `?`()
begin
    declare continue handler for sqlexception begin
    end;
        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','ADMIN');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','MANAGER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','SUPERVISOR');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','USER');

        INSERT INTO `role_permission`(`id`,`created`,`updated`,`version`,`permission`,`role_name`) VALUES (uuid_to_bin(uuid()),sysdate(6),sysdate(6),0,'STOCK_AUDIT_VIEW','STAFF');

end //
delimiter ;
call `?`();
drop procedure if exists `?`;


