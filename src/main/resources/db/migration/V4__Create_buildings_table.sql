CREATE TABLE buildings (
                           id INT NOT NULL AUTO_INCREMENT,
                           building_type VARCHAR(32) NOT NULL,
                           level INT NOT NULL,
                           hp INT NOT NULL,
                           kingdom_id INT NOT NULL,
                           started_at TIMESTAMP NOT NULL,
                           finished_at TIMESTAMP NOT NULL,
                           PRIMARY KEY (id),
                           FOREIGN KEY (kingdom_id) REFERENCES kingdoms(id)
);