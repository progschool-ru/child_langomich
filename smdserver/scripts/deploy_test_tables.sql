CREATE TABLE smd_test1
(
	test_id VARCHAR(38) UNIQUE NOT NULL,
	name VARCHAR(254),
	PRIMARY KEY (test_id)
) ENGINE = InnoDB;  
