INSERT INTO `role_permission`(`id`, `created`, `updated`, `version`, `permission`, `role_name`)
VALUES (uuid_to_bin(uuid()), sysdate(6), sysdate(6), 0, 'PROD_PRICE_UPDATE', 'ADMIN');


INSERT INTO `role_permission`(`id`, `created`, `updated`, `version`, `permission`, `role_name`)
VALUES (uuid_to_bin(uuid()), sysdate(6), sysdate(6), 0, 'PROD_PRICE_UPDATE', 'MANAGER');

