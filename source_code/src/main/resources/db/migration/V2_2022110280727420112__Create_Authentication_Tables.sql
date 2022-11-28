
CREATE TABLE IF NOT EXISTS `user` (
  `id` binary(16) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created` datetime(6) NOT NULL,
  `updated` datetime(6) NOT NULL,
  `version` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_AK_username` (`username`)
);

CREATE TABLE IF NOT EXISTS `role` (
  `id` binary(16) NOT NULL,
  `name` varchar(50) NOT NULL,
  `created` datetime(6) NOT NULL,
  `version` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_AK_name` (`name`)
);

CREATE TABLE IF NOT EXISTS `user_roles` (
  `users_id` binary(16) NOT NULL,
  `roles_id` binary(16) NOT NULL,
  PRIMARY KEY (users_id, roles_id)
);