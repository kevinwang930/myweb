CREATE TABLE `users` (
                         `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名字',
                         `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                         `email_modified_at` timestamp NULL DEFAULT NULL COMMENT '用户修改邮箱时间',
                         `email_verified_at` timestamp NULL DEFAULT NULL COMMENT '邮箱验证时间',
                         `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                         `is_baned` int(11) DEFAULT '0' COMMENT '用户是否被禁用',
                         PRIMARY KEY (`id`) USING BTREE,
                         UNIQUE KEY `users_email_unique` (`email`),
                         UNIQUE KEY `invite_code` (`invite_code`)
) ENGINE=InnoDB AUTO_INCREMENT=322 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;