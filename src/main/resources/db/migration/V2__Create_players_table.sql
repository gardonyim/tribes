CREATE TABLE players (
                         id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL,
                         password VARCHAR(255),
                         avatar VARCHAR(255),
                         points INT
);