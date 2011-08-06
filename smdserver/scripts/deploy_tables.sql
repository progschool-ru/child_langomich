CREATE TABLE smd_users
(
	user_id VARCHAR (36) NOT NULL,
	login VARCHAR (80) UNIQUE NOT NULL,
	psw VARCHAR (32) NOT NULL,
	is_blocked BOOLEAN NOT NULL DEFAULT '1',
	email VARCHAR (80) NOT NULL,
	time_created DATETIME NOT NULL,
	time_modified DATETIME NOT NULL,
	PRIMARY KEY (user_id)
) ENGINE = InnoDB CHARACTER SET = utf8;

CREATE TABLE smd_languages
(
	language_id VARCHAR (36) NOT NULL,
	name VARCHAR (80) NOT NULL,
	user_id VARCHAR(36) NOT NULL,
	time_created DATETIME NOT NULL,
	time_modified DATETIME NOT NULL,
	PRIMARY KEY (language_id),
	FOREIGN KEY (user_id) REFERENCES smd_users(user_id) ON DELETE CASCADE,
	UNIQUE (name, user_id)
)  ENGINE = InnoDB CHARACTER SET = utf8;

CREATE TABLE smd_words
(
	language_id VARCHAR (36) NOT NULL,
	original VARCHAR (80) NOT NULL,
	translation VARCHAR(80) NOT NULL,
	rating INTEGER NOT NULL,
	modified BIGINT NOT NULL,
	time_created DATETIME NOT NULL,
	time_modified DATETIME NOT NULL,
	PRIMARY KEY (language_id, original),
	FOREIGN KEY (language_id) REFERENCES smd_languages(language_id) ON DELETE CASCADE
)  ENGINE = InnoDB CHARACTER SET = utf8;