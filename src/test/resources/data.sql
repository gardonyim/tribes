DELETE FROM kingdoms;
DELETE FROM players;
INSERT INTO players (id, username, password) VALUES (1, 'existingtestuser', '$2a$10$b7wnqdlcepRbdn.PFKww8e11aZaLdZtOGl8RVNkd1mKFuWRzA/IpK');
INSERT INTO kingdoms VALUES (1, 'existingkingdom', 1);