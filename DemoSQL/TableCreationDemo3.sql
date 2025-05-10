drop database ymcafinal; -- if table already exists
create database ymcafinal;
use ymcafinal;

CREATE TABLE User (
    user_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(100),
    user_type VARCHAR(50),
    balance DECIMAL(10,2),
    status VARCHAR(50) DEFAULT 'active'
);

CREATE TABLE FamilyMember (
    user_id BIGINT NOT NULL,
    family_member_id BIGINT NOT NULL,
    relationship VARCHAR(50),
    PRIMARY KEY (user_id, family_member_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (family_member_id) REFERENCES User(user_id)
);

CREATE TABLE Dependent (
    dependent_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthdate DATE,
    relationship VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

CREATE TABLE Inbox (
    message_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT,
    date_sent DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

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
    requirements TEXT,     
    days VARCHAR(100),
    staff_id BIGINT,       
    FOREIGN KEY (staff_id) REFERENCES User(user_id)
);


CREATE TABLE Registration (
    registration_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    program_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    participant_user_id BIGINT,
    participant_dependent_id BIGINT,
    registration_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (program_id) REFERENCES Program(program_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (participant_user_id) REFERENCES User(user_id),
    FOREIGN KEY (participant_dependent_id) REFERENCES Dependent(dependent_id),
    CHECK (
        (participant_user_id IS NOT NULL AND participant_dependent_id IS NULL)
        OR (participant_user_id IS NULL AND participant_dependent_id IS NOT NULL)
    )
);

INSERT INTO User (username, password, email, user_type, balance) VALUES
('janedoe', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'jane@example.com', 'member', 200.00),       -- user_id = 1
('lukeanderson', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'luke@example.com', 'member', 200.00), -- user_id = 2
('ainianderson', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'aini@example.com', 'member', 200.00), -- user_id = 3
('bobsmith', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'bob@example.com', 'staff', 200.00),
('admin', 'bed4efa1d4fdbd954bd3705d6a2a78270ec9a52ecfbfb010c61862af5c76af1761ffeb1aef6aca1bf5d02b3781aa854fabd2b69c790de74e17ecfec3cb6ac4bf', 'admin@ymca.com', 'admin', 200.00);       -- user_id = 4

INSERT INTO FamilyMember (user_id, family_member_id, relationship) VALUES 
(2, 3, 'sibling'),
(3, 2, 'sibling');

INSERT INTO Program (
    program_name, description, capacity, current_capacity,
    start_date, end_date, start_time, end_time,
    location, price, requirements, days, staff_id
) VALUES (
    'Pike', 'Entry-level swim class', 10, 0,
    '2025-03-01', '2025-04-15', '17:00:00', '17:30:00',
    'YMCA Onalaska pool', 80.00, NULL, 'Tuesday,Thursday', 4
);

-- Shark (Sunday)
INSERT INTO Program (
    program_name, description, capacity, current_capacity,
    start_date, end_date, start_time, end_time,
    location, price, requirements, days, staff_id
) VALUES (
    'Shark', 'Advanced swim class', 8, 0,
    '2025-05-18', '2025-06-22', '17:00:00', '17:40:00',
    'YMCA Onalaska pool', 96.00, '1', 'Sunday', 4
);

-- Shark (Mon & Wed)
INSERT INTO Program (
    program_name, description, capacity, current_capacity,
    start_date, end_date, start_time, end_time,
    location, price, requirements, days, staff_id
) VALUES (
    'Shark', 'Advanced swim class', 8, 0,
    '2025-05-18', '2025-06-22', '18:00:00', '18:40:00',
    'YMCA Onalaska pool', 130.00, '1', 'Monday,Wednesday', 4
);

-- Log Rolling (Sunday)
INSERT INTO Program (
    program_name, description, capacity, current_capacity,
    start_date, end_date, start_time, end_time,
    location, price, requirements, days, staff_id
) VALUES (
    'Log Rolling', 'Balance and fun in water', 1, 0,
    '2025-05-18', '2025-06-22', '17:00:00', '17:40:00',
    'YMCA Onalaska pool', 200.00, NULL, 'Sunday', 4
);

-- Log Rolling (Monday)
INSERT INTO Program (
    program_name, description, capacity, current_capacity,
    start_date, end_date, start_time, end_time,
    location, price, requirements, days, staff_id
) VALUES (
    'Log Rolling', 'Balance and fun in water', 2, 0,
    '2025-05-18', '2025-06-22', '18:00:00', '18:40:00',
    'YMCA Onalaska pool', 200.00, NULL, 'Monday', 4
);

-- Jane Doe
INSERT INTO Registration (program_id, user_id, participant_user_id, participant_dependent_id) VALUES 
(2, 1, 1, NULL), -- Shark Sunday
(5, 1, 1, NULL); -- Log Rolling Monday

-- Luke Anderson
INSERT INTO Registration (program_id, user_id, participant_user_id, participant_dependent_id) VALUES 
(3, 2, 2, NULL), -- Shark Mon/Wed
(4, 2, 2, NULL); -- Log Rolling Sunday

-- Aini Anderson
INSERT INTO Registration (program_id, user_id, participant_user_id, participant_dependent_id) VALUES 
(5, 3, 3, NULL); -- Log Rolling Monday

-- Bob Smith (staff)
INSERT INTO Registration (program_id, user_id, participant_user_id, participant_dependent_id) VALUES 
(4, 4, 4, NULL); -- Log Rolling Sunday
