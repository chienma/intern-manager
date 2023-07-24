CREATE TABLE mentors
(
    mentor_id     SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    phone_number  CHAR(10),
    email         VARCHAR(255) NOT NULL UNIQUE,
    position      VARCHAR(255)
);

CREATE TABLE interns
(
    intern_id     SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    phone_number  CHAR(10),
    email         VARCHAR(255) NOT NULL UNIQUE,
    position      VARCHAR(255),
    mentor_id     INT REFERENCES mentors (mentor_id)
);


