DROP TABLE IF EXISTS departments_lectors;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS lectors;

CREATE TABLE lectors
(
    id      serial PRIMARY KEY,
    name    VARCHAR(255),
    surname VARCHAR(255),
    degree  VARCHAR(255),
    salary  INT
);

CREATE TABLE departments
(
    id   serial PRIMARY KEY,
    name VARCHAR(255),
    head INT,
    CONSTRAINT FK_departments_to_lectors FOREIGN KEY (head) REFERENCES lectors (id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE departments_lectors
(
    department_id INTEGER REFERENCES departments (id) ON UPDATE CASCADE ON DELETE CASCADE,
    lector_id     INTEGER REFERENCES lectors (id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (department_id, lector_id)
);