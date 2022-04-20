CREATE TABLE players (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(80) NOT NULL,
    kingdom_id INT NOT NULL,
    avatar VARCHAR(50) NOT NULL,
    points INT NOT NULL
);