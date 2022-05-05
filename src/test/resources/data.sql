DELETE FROM buildings;
DELETE FROM troops;
DELETE FROM resources;
DELETE FROM kingdoms;
DELETE FROM locations;
DELETE FROM players;

INSERT INTO players (id, username, password) VALUES
    (1, 'existingtestuser', '$2a$10$b7wnqdlcepRbdn.PFKww8e11aZaLdZtOGl8RVNkd1mKFuWRzA/IpK'),
    (2, 'testuser2', '$2a$10$b7wnqdlcepRbdn.PFKww8e11aZaLdZtOGl8RVNkd1mKFuWRzA/IpK'),
    (3, 'testuser3', '$2a$10$b7wnqdlcepRbdn.PFKww8e11aZaLdZtOGl8RVNkd1mKFuWRzA/IpK'),
    (4, 'testuser4', '$2a$10$b7wnqdlcepRbdn.PFKww8e11aZaLdZtOGl8RVNkd1mKFuWRzA/IpK');

INSERT INTO kingdoms VALUES
    (1, 'existingkingdom', 1, 1),
    (2, 'testkingdom2', 2, 2);
    (3, 'testkingdom3', 3, 3);
    (4, 'testkingdom4', 4, 4);

INSERT INTO locations VALUES
    (1, 0, 0),
    (2, 5, -5),
    (3, 5, -6),
    (4, 11, -5);

INSERT INTO resources VALUES
    (1, 'gold', 50, 0, '2022-05-01 18:48:42', 3),
    (2, 'gold', 150, 0, '2022-05-01 18:48:42', 4);

INSERT INTO buildings VALUES
    (1, 'townhall', 1, 200, 1, '2022-05-01 18:48:42', '2022-05-01 18:48:42'),
    (2, 'townhall', 0, 200, 2, '2022-05-01 18:48:42', '2022-05-01 18:48:42'),
    (3, 'townhall', 1, 200, 3, '2022-05-01 18:48:42', '2022-05-01 18:48:42'),
    (4, 'townhall', 1, 200, 4, '2022-05-01 18:48:42', '2022-05-01 18:48:42'),
    (5, 'academy', 1, 1, 1, '2022-05-02 18:40:45', '2022-05-02 18:43:49'),
    (6, 'mine', 1, 1, 1, '2022-05-02 18:40:45', '2022-05-02 18:43:49');

INSERT INTO troops (id, level, hp, attack, defence, started_at, finished_at, kingdom_id) VALUES
    (1, 1, 1, 1, 1, '2022-05-01 22:23:49', '2022-05-01 22:23:44', 1);
