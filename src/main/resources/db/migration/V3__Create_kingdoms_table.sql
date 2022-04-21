CREATE TABLE kingdoms (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50),
    player_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (player_id) REFERENCES players(id)
);