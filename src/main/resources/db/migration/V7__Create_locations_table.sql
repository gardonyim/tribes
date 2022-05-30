CREATE TABLE locations (
    id INT NOT NULL AUTO_INCREMENT,
    x_coordinate INT NOT NULL,
    y_coordinate INT NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE kingdoms ADD location_id INT;
ALTER TABLE kingdoms ADD FOREIGN KEY (location_id) REFERENCES locations(id);