CREATE TABLE IF NOT EXISTS users_table (
    user_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS groups_table (
    group_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS chats_table (
    chat_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    group_id BIGINT UNSIGNED NOT NULL,

    FOREIGN KEY (group_id)
        REFERENCES groups_table(group_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages_table (
    message_id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT UNSIGNED NOT NULL,
    chat_id BIGINT UNSIGNED,
    content TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users_table(user_id),
    FOREIGN KEY (chat_id) REFERENCES chats_table(chat_id)
);

CREATE TABLE IF NOT EXISTS group_members (
    group_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,

    PRIMARY KEY (group_id, user_id),

    FOREIGN KEY (group_id)
        REFERENCES groups_table(group_id)
        ON DELETE CASCADE,

    FOREIGN KEY (user_id)
        REFERENCES users_table(user_id)
        ON DELETE CASCADE
);

