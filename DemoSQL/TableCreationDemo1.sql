DROP DATABASE ymcatest_demo1; -- if table already exists
CREATE DATABASE ymcatest_demo1;
USE ymcatest_demo1;

-- Create the User table with balance as DECIMAL(10,2)
CREATE TABLE User (
    user_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(255),
    email VARCHAR(100),
    user_type VARCHAR(50),
    balance DECIMAL(10,2)
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
    requirements INT
);

-- Create the Registration table with foreign keys
CREATE TABLE Registration (
    registration_id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    program_id BIGINT,
    registration_date DATE,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (program_id) REFERENCES Program(program_id)
);

-- Insert sample Users (10 Members, 5 Staff, 5 Users) with balance as DECIMAL values
INSERT INTO User (username, password, email, user_type, balance) VALUES
('john_doe', 'password123', 'john.doe@example.com', 'Member', 150),
('jane_smith', 'password456', 'jane.smith@example.com', 'Staff', 200),
('bob_johnson', 'password789', 'bob.johnson@example.com', 'Member', 150),
('member1', 'password1', 'member1@example.com', 'Member', 100.00),
('member2', 'password2', 'member2@example.com', 'Member', 110.00),
('member3', 'password3', 'member3@example.com', 'Member', 120.00),
('member4', 'password4', 'member4@example.com', 'Member', 130.00),
('member5', 'password5', 'member5@example.com', 'Member', 140.00),
('member6', 'password6', 'member6@example.com', 'Member', 150.00),
('member7', 'password7', 'member7@example.com', 'Member', 160.00),
('member8', 'password8', 'member8@example.com', 'Member', 170.00),
('member9', 'password9', 'member9@example.com', 'Member', 180.00),
('member10', 'password10', 'member10@example.com', 'Member', 190.00),
('staff1', 'password1', 'staff1@example.com', 'Staff', 0.00),
('staff2', 'password2', 'staff2@example.com', 'Staff', 0.00),
('staff3', 'password3', 'staff3@example.com', 'Staff', 0.00),
('staff4', 'password4', 'staff4@example.com', 'Staff', 0.00),
('staff5', 'password5', 'staff5@example.com', 'Staff', 0.00),
('user1', 'password1', 'user1@example.com', 'User', 50.00),
('user2', 'password2', 'user2@example.com', 'User', 50.00),
('user3', 'password3', 'user3@example.com', 'User', 50.00),
('user4', 'password4', 'user4@example.com', 'User', 50.00),
('user5', 'password5', 'user5@example.com', 'User', 50.00);

-- Insert sample Programs
INSERT INTO Program (program_name, description, capacity, current_capacity, start_date, end_date, start_time, end_time, location, price, requirements) VALUES
('Yoga Class', 'A relaxing yoga session for all levels.', 20, 0, '2025-04-01', '2025-04-01', '09:00:00', '10:00:00', 'Room1', 40.00, NULL),
('Cooking Workshop', 'Learn how to cook gourmet meals.', 15, 0, '2025-05-01', '2025-05-01', '10:00:00', '11:00:00', 'Room2', 100.00, NULL),
('Dance Class', 'Learn the basics of contemporary dance.', 25, 0, '2025-06-01', '2025-06-01', '14:00:00', '14:40:00', 'Room3', 60.00, NULL),
('Swimming - Pike', 'Sundays. A beginner swimming class focusing on technique.', 30, 0, '2025-07-01', '2025-08-01', '08:00:00', '08:50:00', 'YMCA Swimming Pool', 16.00, NULL);

-- Insert sample Registrations
INSERT INTO Registration (user_id, program_id, registration_date) VALUES
(1, 1, '2025-03-01'),  -- John Doe registered for Yoga Class
(2, 2, '2025-03-02'),  -- Jane Smith registered for Cooking Workshop
(3, 3, '2025-03-03');  -- Bob Johnson registered for Dance Class