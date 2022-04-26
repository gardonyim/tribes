CREATE TABLE buildings (
                           id INT NOT NULL AUTO_INCREMENT,
                           building_type VARCHAR(32) NOT NULL,
                           level INT NOT NULL,
                           hp INT NOT NULL,
                           kingdom_id INT NOT NULL,
                           startedAt TIMESTAMP NOT NULL,
                           finishedAt TIMESTAMP NOT NULL,
                           PRIMARY KEY (id),
                           FOREIGN KEY (kingdom_id) REFERENCES kingdoms(id)
);