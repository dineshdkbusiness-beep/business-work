INSERT INTO usersrecords ( username, created_date, user_type, is_alive, user_deleted, expire_date, login_attempts, password, email, active_token
)
SELECT 'TestAdmin', NOW(), 'S', true, false, NULL, 0, 'system123#', 'admin@example.com', NULL
WHERE NOT EXISTS ( SELECT 1 FROM usersrecords WHERE user_index = 1
);