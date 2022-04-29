CREATE TABLE game_rules (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    game_object_type varchar(30) NOT NULL,
    building_time_multiplier_1_in_sec INT NOT NULL,
    building_time_multiplier_n_in_sec INT NOT NULL,
    building_cost_multiplier_1 INT NOT NULL,
    building_cost_multiplier_n INT NOT NULL,
    hp_multiplier INT NOT NULL
);

INSERT INTO game_rules (
    game_object_type, building_time_multiplier_1_in_sec, building_time_multiplier_n_in_sec,
    building_cost_multiplier_1, building_cost_multiplier_n, hp_multiplier)
    VALUES
    ('townhall', 120, 60, 200, 200, 200),
    ('farm', 60, 60, 100, 100, 100),
    ('mine', 60, 60, 100, 100, 100),
    ('academy', 90, 60, 150, 100, 150),
    ('troop', 30, 30, 25, 25, 20);
