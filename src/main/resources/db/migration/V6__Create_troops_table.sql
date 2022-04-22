CREATE TABLE troops (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    level INT NOT NULL,
    hp INT NOT NULL,
    attack INT NOT NULL,
    defence INT NOT NULL,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,
    kingdom_id INT NOT NULL,
    FOREIGN KEY (kingdom_id) REFERENCES kingdoms(id)
);