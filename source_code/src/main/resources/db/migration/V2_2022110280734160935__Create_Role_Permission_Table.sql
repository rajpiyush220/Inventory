
CREATE TABLE IF NOT EXISTS `role_permission` (
    `id` binary(16) NOT NULL,
    `created` datetime(6) NOT NULL,
    `updated` datetime(6) NOT NULL,
    `version` bigint NOT NULL,
    `permission` varchar(255) DEFAULT NULL,
    `role_name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `role_permission_role_name_id` (`role_name`)
);