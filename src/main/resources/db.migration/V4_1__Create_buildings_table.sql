CREATE TABLE buildings (
                         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         building_type VARCHAR(32) NOT NULL,
                         level INT NOT NULL UNSIGNED,
                         hp INT NOT NULL UNSIGNED,
                         startedAt INT NOT NULL,
                         finishedAt INT
);