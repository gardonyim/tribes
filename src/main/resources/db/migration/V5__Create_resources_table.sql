CREATE TABLE resources (
    id INT NOT NULL AUTO_INCREMENT,
    resource_type VARCHAR(20),
    amount INT NOT NULL,
    generation INT NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    kingdom_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (kingdom_id) REFERENCES kingdoms(id)
);