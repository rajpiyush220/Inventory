CREATE TABLE IF NOT EXISTS `password_reset_token` (
  `id` binary(16) NOT NULL,
  `created` datetime(6) NOT NULL,
  `version` bigint NOT NULL,
  `token` binary(16) NOT NULL,
  `expired_at` datetime(6) NOT NULL,
  `user_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY `FK_password_reset_token_user` (user_id) REFERENCES user(id)
);