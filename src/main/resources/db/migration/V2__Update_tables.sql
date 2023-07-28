DROP TABLE interns;
DROP TABLE mentors;

CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    phone_number  CHAR(10),
    email         VARCHAR(255) NOT NULL UNIQUE,
    position      VARCHAR(255)
);

CREATE TABLE mentors
(
    id INT PRIMARY KEY REFERENCES users (id)
);

CREATE TABLE interns
(
    id        INT PRIMARY KEY REFERENCES users (id),
    mentor_id INT REFERENCES mentors (id)
);