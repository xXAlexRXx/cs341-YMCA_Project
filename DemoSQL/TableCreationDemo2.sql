DROP DATABASE ymcatest_demo2; -- if table already exists
CREATE DATABASE ymcatest_demo2;
USE ymcatest_demo2;

CREATE TABLE User (
    user_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(100),
    user_type VARCHAR(50),
    balance DECIMAL(10,2)
);

CREATE TABLE Inbox (
    message_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT,
    date_sent DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE FamilyMember (
    user_id BIGINT NOT NULL,
    family_member_id BIGINT NOT NULL,
    relationship VARCHAR(50),
    PRIMARY KEY (user_id, family_member_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (family_member_id) REFERENCES User(user_id)
);

-- Create the Program table with a price column of DECIMAL(10,2)
CREATE TABLE Program (
    program_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    program_name VARCHAR(100),
    description TEXT,
    capacity INT,
    current_capacity INT,
    start_date DATE,
    end_date DATE,
    start_time TIME,
    end_time TIME,
    location VARCHAR(100),
    price DECIMAL(10,2),
    requirements INT,
    days VARCHAR(21),
    staff_id BIGINT,
    FOREIGN KEY (staff_id) REFERENCES User(user_id)
);

-- Create the Registration table with foreign keys
CREATE TABLE Registration (
    registration_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    participant_id BIGINT NOT NULL,
    program_id BIGINT NOT NULL,
    registration_date DATE,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (participant_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (program_id) REFERENCES Program(program_id) ON DELETE CASCADE
);

DELIMITER $$

CREATE TRIGGER trg_after_registration_insert
AFTER INSERT ON Registration
FOR EACH ROW
BEGIN
    UPDATE Program 
    SET current_capacity = current_capacity + 1
    WHERE program_id = NEW.program_id;
END$$

DELIMITER ;

-- Staff
INSERT INTO User (username, password, email, user_type, balance) VALUES
('staff1', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'staff1@ymca.com', 'Staff', 0.00);

INSERT INTO User (username, password, email, user_type, balance) VALUES
('staff2', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'staff1@ymca.com', 'Staff', 0.00);

-- Members and non-members
INSERT INTO User (username, password, email, user_type, balance) VALUES
('janedoe', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'janedoe@ymca.com', 'Member', 100.00),
('lukeanderson', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'lukeanderson@gmail.com', 'Non-Member', 100.00),
('ainianderson', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'ainianderson@ymca.com', 'Member', 100.00),
('annianderson', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'anni@ymca.com', 'Member', 50.00),
('luckanderson', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'luck@ymca.com', 'Member', 50.00);

-- Family relationships
INSERT INTO FamilyMember (user_id, family_member_id, relationship) VALUES
((SELECT user_id FROM User WHERE username = 'ainianderson'), (SELECT user_id FROM User WHERE username = 'annianderson'), 'child'),
((SELECT user_id FROM User WHERE username = 'ainianderson'), (SELECT user_id FROM User WHERE username = 'luckanderson'), 'child');

INSERT INTO Program (program_name, description, capacity, current_capacity, start_date, end_date, start_time, end_time, location, price, requirements, days, staff_id) VALUES
('Kids Swim Class', 'Swimming lessons for beginners.', 20, 0, '2025-05-10', '2025-06-10', '10:00:00', '11:00:00', 'YMCA Pool', 80.00, NULL, 'Mon', 6), -- taught by staff1 (user_id 6)
('Adult Yoga', 'Relaxing yoga session for adults.', 15, 0, '2025-05-15', '2025-06-15', '09:00:00', '10:00:00', 'Room 1', 60.00, NULL, 'MonWedFri', 7); 