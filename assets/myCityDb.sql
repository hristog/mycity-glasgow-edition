-- =================================================================================================================
-- Create all tables
-- -----------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS "GameBoard";
CREATE TABLE GameBoard(
   edition_name VARCHAR(40), 
   top_left_x_coord INTEGER NOT NULL, -- GeoPoint
   top_left_y_coord INTEGER NOT NULL, -- GeoPoint
   bottom_right_x_coord INTEGER NOT NULL, -- GeoPoint
   bottom_right_y_coord INTEGER NOT NULL, -- GeoPoint
   PRIMARY KEY(edition_name)
);

DROP TABLE IF EXISTS "Users";
CREATE TABLE Users(
   id INTEGER,
   token_id INTEGER NOT NULL DEFAULT(0),
   house_type INTEGER NOT NULL DEFAULT(0),
   mycity_points INTEGER NOT NULL DEFAULT(0),
   overall_score INTEGER NOT NULL DEFAULT(0),
   num_properties INTEGER NOT NULL DEFAULT(0),
   num_castles INTEGER NOT NULL DEFAULT(0),
   name VARCHAR(20) NOT NULL,
   email VARCHAR(30) NOT NULL,
   pass VARCHAR(20) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE (id, email)
);

DROP TABLE IF EXISTS "Activity";
CREATE TABLE Activity(
   user_id INTEGER,
   week INTEGER NOT NULL,
   year INTEGER NOT NULL, 
   type INTEGER NOT NULL DEFAULT(0), -- 0: remaining, 1: walking, 2: running, 3: cycling, 4: swimming, 5: sedentary
   duration INTEGER NOT NULL,
   PRIMARY KEY(user_id, week, year, type),
   FOREIGN KEY(user_id) REFERENCES Users(id)
);

DROP TABLE IF EXISTS "Properties";
CREATE TABLE Properties(
   id INTEGER,
   x_coord INTEGER NOT NULL, -- GeoPoint
   y_coord INTEGER NOT NULL, -- GeoPoint
   x_grid INTEGER NOT NULL,
   y_grid INTEGER NOT NULL,
   title VARCHAR(20) NOT NULL,
   drawable_id INTEGER NOT NULL,
   description VARCHAR(128),
   owner_id INTEGER,
   PRIMARY KEY(id),
   FOREIGN KEY(owner_id) REFERENCES Users(id),
   UNIQUE (id, x_coord, y_coord)
);

DROP TABLE IF EXISTS "Friends";
CREATE TABLE Friends(
   user1_id INTEGER NOT NULL,
   user2_id INTEGER NOT NULL,
   PRIMARY KEY(user1_id, user2_id),
   FOREIGN KEY(user1_id) REFERENCES Users(id),
   FOREIGN KEY(user2_id) REFERENCES Users(id)
);

DROP TABLE IF EXISTS "Requests";
CREATE TABLE Requests(
   to_user_id INTEGER NOT NULL, -- request to
   from_user_id INTEGER NOT NULL, -- sent by
   type INTEGER NOT NULL, -- 0: friend, 1: challenge, 2: group
   reference_id INTEGER DEFAULT(0) NOT NULL,
   response INTEGER DEFAULT(0) NOT NULL, -- 0: not responded yet, 1: accepted, 2: rejected
   PRIMARY KEY(to_user_id, from_user_id, type, reference_id),
   FOREIGN KEY(to_user_id) REFERENCES Users(id),
   FOREIGN KEY(from_user_id) REFERENCES Users(id)
);

-- DROP TABLE IF EXISTS "Groups";
-- CREATE TABLE Groups(
--    id INTEGER,
--    name VARCHAR(30) NOT NULL,
--    description VARCHAR(128),
--    PRIMARY KEY(id),
--    UNIQUE (id, name)
-- );

-- DROP TABLE IF EXISTS "UsersInGroups";
-- CREATE TABLE UsersInGroups (
--    group_id INTEGER,
--    user_id INTEGER,
--    PRIMARY KEY(group_id, user_id),
--    FOREIGN KEY(group_id) REFERENCES Groups(id),
--    FOREIGN KEY(user_id) REFERENCES Users(id)
-- );

DROP TABLE IF EXISTS "Collectibles";
CREATE TABLE Collectibles(
   id INTEGER,
   x_coord INTEGER DEFAULT (0) NOT NULL, -- GeoPoint
   y_coord INTEGER DEFAULT (0) NOT NULL, -- GeoPoint
   x_grid INTEGER DEFAULT (0) NOT NULL,
   y_grid INTEGER DEFAULT (0) NOT NULL,
   name VARCHAR(30) NOT NULL,
   type INTEGER NOT NULL, -- 1: park card (qty: -1), 2: city card (qty: -1), 3: mycity points card (qty: -1), 4: customisation card (qty: -1)
   description VARCHAR(128) NOT NULL,
   drawable_id INTEGER DEFAULT(0) NOT NULL,
   quantity INTEGER DEFAULT (-1) NOT NULL,
   PRIMARY KEY(id),
   UNIQUE (id, x_coord, y_coord)
);

DROP TABLE IF EXISTS "CollectedItems";
CREATE TABLE CollectedItems(
   user_id INTEGER NOT NULL,
   collectible_id INTEGER NOT NULL,
   instances_owned INTEGER DEFAULT(1) NOT NULL,
   renewable_after_day INTEGER DEFAULT(-1) NOT NULL, -- day of year
   renewable_after_year INTEGER DEFAULT (-1) NOT NULL, -- year
   PRIMARY KEY (user_id, collectible_id),
   FOREIGN KEY (user_id) REFERENCES Users(id),
   FOREIGN KEY (collectible_id) REFERENCES Collectibles(id)
); 

DROP TABLE IF EXISTS "Challenges";
CREATE TABLE Challenges(
   id INTEGER,
   name VARCHAR(30) NOT NULL,
   type INTEGER NOT NULL, -- ?
   description VARCHAR(128),
   PRIMARY KEY(id)
);

DROP TABLE IF EXISTS "ChallengesCompleted";
CREATE TABLE ChallengesCompleted(
   challenge_id INTEGER,
   user_id INTEGER,
   PRIMARY KEY(challenge_id, user_id),
   FOREIGN KEY(challenge_id) REFERENCES Challenges(id),
   FOREIGN KEY(user_id) REFERENCES Users(id)
);

-- =================================================================================================================
-- Define triggers
-- -----------------------------------------------------------------------------------------------------------------

-- Increase the number of properties for a given user upon building a new property owned by that user
CREATE TRIGGER "num_properties_after_insert" AFTER INSERT ON "Properties" FOR EACH ROW BEGIN UPDATE Users SET num_properties = num_properties + 1 WHERE id = NEW.owner_id; END;

-- Update the number of properties for a given user upon change of ownership (the property counts for both the old owner and the new owner are updated)
CREATE TRIGGER "num_properties_after_update" AFTER UPDATE ON "Properties" FOR EACH ROW WHEN OLD.owner_id <> NEW.owner_id BEGIN UPDATE Users SET num_properties = num_properties + 1 WHERE id = NEW.owner_id; UPDATE Users SET num_properties = num_properties - 1 WHERE id = OLD.owner_id; END;

-- Update the overall score for a given user upon change in MyCity points, number of properties or number of castles
-- {overall_score} = {mycity_points} + 2 * {mycity_points used for building properties} + 3 * {mycity_points used for building castles}
CREATE TRIGGER "update_overall_score" AFTER UPDATE ON "Users" FOR EACH ROW WHEN OLD.mycity_points <> NEW.mycity_points OR OLD.num_properties <> NEW.num_properties OR OLD.num_castles <> NEW.num_castles BEGIN UPDATE Users SET overall_score = mycity_points + num_properties * 1000 + num_castles * 1500 WHERE id = OLD.id; END;

-- Update the number of properties for a given user after a building has been removed
CREATE TRIGGER "num_properties_after_delete" AFTER DELETE ON "Properties" FOR EACH ROW BEGIN UPDATE Users SET num_properties = num_properties - 1 WHERE id = OLD.owner_id; END;

-- Upon collection of an item its total quantity (if available in a finite number) is updated
CREATE TRIGGER "update_item_quantity" AFTER INSERT ON "CollectedItems" FOR EACH ROW BEGIN UPDATE Collectibles SET quantity = quantity - 1 WHERE quantity > 0 AND id = NEW.collectible_id; END;

-- Upon creation of a new frienship, the connection between them is reflected so that the relationship is double-sided
CREATE TRIGGER "reflect_friendship" AFTER INSERT ON "Friends" FOR EACH ROW BEGIN INSERT INTO FRIENDS (user1_id, user2_id) VALUES (NEW.user2_id, NEW.user1_id); END;

-- Upon acceptance of a friendship request, automatically insert a new row in the Friends table to indicate the connection (then the reflection trigger is automatically executed)
CREATE TRIGGER "accept_friendship" AFTER UPDATE ON "Requests" FOR EACH ROW WHEN OLD.type = 0 AND NEW.repose = 1 BEGIN INSERT INTO FRIENDS (user1_id, user2_id) VALUES (OLD.from_user_id, OLD.to_user_id); END;

-- Upon removal of a friendship connection, delete the other side of the relationship as well
CREATE TRIGGER "remove_friendship" AFTER DELETE ON "Friends" FOR EACH ROW BEGIN DELETE FROM FRIENDS WHERE user1_id = OLD.user2_id AND user2_id = OLD.user1_id; END;


-- =================================================================================================================
-- Populate the Collectibles table
-- -----------------------------------------------------------------------------------------------------------------

-- Coordinates for the Glasgow Edition game board

INSERT INTO "GameBoard" (edition_name, top_left_x_coord, top_left_y_coord, bottom_right_x_coord, bottom_right_y_coord) VALUES ('Glasgow', 55946252, -4411040, 55671712, -3803212);

-- MyCity Points
INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(0, 0, 0, 0, '100 MyCity Points', 3, 'This is a MyCity Points card for the amount of 100 points.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(0, 0, 0, 0, '250 MyCity Points', 3, 'This is a MyCity Points card for the amount of 250 points.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(0, 0, 0, 0, '500 MyCity Points', 3, 'This is a MyCity Points card for the amount of 500 points.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(0, 0, 0, 0, '1000 MyCity Points', 3, 'This is a MyCity Points card for the amount of 1000 points.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(0, 0, 0, 0, '2500 MyCity Points', 3, 'This is a MyCity Points card for the amount of 2500 points.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(0, 0, 0, 0, '5000 MyCity Points', 3, 'This is a MyCity Points card for the amount of 5000 points.');

-- Museums

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55865597, -4305252, 0, 0, 'Riverside Museum City Card', 2, 'This is the Riverside Museum City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55868553, -4290854, 0, 0, 'Kelvingrove Art Gallery and Museum City Card', 2, 'This is the Kelvingrove Art Gallery and Museum City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55830891, -4307414, 0, 0, 'The Burrell Collection City Card', 2, 'This is the The Burrell Collection City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55860064, -4251889, 0, 0, 'Gallery of Modern Art (GoMA) City Card', 2, 'This is the Gallery of Modern Art (GoMA) City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55862333, -4236780, 0, 0, 'Provand''s Lordship City Card', 2, 'This is the Provand''s Lordship City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55851251, -4236842, 0, 0, 'People''s Palace City Card', 2, 'This is the People''s Palace City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55849847, -4273584, 0, 0, 'Scotland Street School Museum City Card', 2, 'This is the Scotland Street School Museum City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55862321, -4236365, 0, 0, 'St Mungo Museum of Religious Life and Art City Card', 2, 'This is the St Mungo Museum of Religious Life and Art City card. It indicates that each holder has visited this great landmark of Glasgow.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55811412, -4365515, 0, 0, 'Glasgow Museums Resource Centre (GMRC) City Card', 2, 'This is the Glasgow Museums Resource Centre (GMRC) City card. It indicates that each holder has visited this place in Glasgow.');

-- Parks

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55865025, -4204202, 0, 0, 'Alexandra Park Card', 1, 'This is the Alexandra Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55870124, -4130488, 0, 0, 'Auchinlea Park Card', 1, 'This is the Auchinlea Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55845080, -4317852, 0, 0, 'Bellahouston Park Card', 1, 'This is the Bellahouston Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55878691, -4289002, 0, 0, 'Botanic Gardens Card', 1, 'This is the Botanic Gardens card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55796216, -4210746, 0, 0, 'Cathkin Braes Country Park Card', 1, 'This is the Cathkin Braes Country Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55806268, -4348093, 0, 0, 'Dams to Darnley Park Card', 1, 'This is the Dams to Darnley park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55897032, -4311980, 0, 0, 'Dawsholm Park Card', 1, 'This is the Dawsholm Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55853117, -4243263, 0, 0, 'Glasgow Green Park Card', 1, 'This is the Glasgow Green park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55877996, -4169448, 0, 0, 'Hogganfield Park Card', 1, 'This is the Hogganfield Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55868169, -4283710, 0, 0, 'Kelvingrove Park Card', 1, 'This is the Kelvingrove Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55804952, -4262384, 0, 0, 'Linn Park Card', 1, 'This is the Linn Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55832523, -4307089, 0, 0, 'Pollok Country Park Card', 1, 'This is the Pollok Country Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55832229, -4269700, 0, 0, 'Queen''s Park Card', 1, 'This is the Queen''s Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55884191, -4273190, 0, 0, 'Ruchill Park Card', 1, 'This is the Ruchill Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55889448, -4224464, 0, 0, 'Springburn Park Card', 1, 'This is the Springburn Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55848183, -4176794, 0, 0, 'Tollcross Park Card', 1, 'This is the Tollcross Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');

INSERT INTO "Collectibles" (x_coord, y_coord, x_grid, y_grid, name, type, description) VALUES(55875422, -4328880, 0, 0, 'Victoria Park Card', 1, 'This is the Victoria Park card. Once you have added this item to your collection, it can be used at any time, but only once at a time. If you would like to use it again afterward, you can re-new the card from the following day on from its original collection location. Upon using a collected chance card, you may win MyCity points, or unlock a new avatar.');


-- =================================================================================================================
-- Populate the Challenges table
-- -----------------------------------------------------------------------------------------------------------------

-- objectives 1, 2, 3, 4


-- =================================================================================================================
-- Test Data
-- -----------------------------------------------------------------------------------------------------------------

INSERT INTO "Users" (id, name, email, pass) VALUES (1, 'Test User1', 'user1@email.com', '+CLuLoHq57U=');

INSERT INTO "Activity" (user_id, week, year, type, duration) VALUES(1, 30, 2012, 1, 30);
INSERT INTO "Activity" (user_id, week, year, type, duration) VALUES(1, 31, 2012, 3, 30);
INSERT INTO "Activity" (user_id, week, year, type, duration) VALUES(1, 32, 2012, 2, 30);

-- add friends etc.
