CREATE TABLE buildings (
                         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         type VARCHAR(20) NOT NULL,
                         level INT NOT NULL UNSIGNED,
                         hp INT NOT NULL UNSIGNED,
                         startedAt INT NOT NULL,
                         finishedAt INT
);