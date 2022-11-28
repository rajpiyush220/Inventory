-- insert role in role db
INSERT INTO `role` VALUES (uuid_to_bin(uuid()),'SUPER_ADMIN',current_timestamp,0);
INSERT INTO `role` VALUES (uuid_to_bin(uuid()),'ADMIN',current_timestamp,0);
INSERT INTO `role` VALUES (uuid_to_bin(uuid()),'MANAGER',current_timestamp,0);
INSERT INTO `role` VALUES (uuid_to_bin(uuid()),'SUPERVISOR',current_timestamp,0);
INSERT INTO `role` VALUES (uuid_to_bin(uuid()),'USER',current_timestamp,0);
INSERT INTO `role` VALUES (uuid_to_bin(uuid()),'STAFF',current_timestamp,0);