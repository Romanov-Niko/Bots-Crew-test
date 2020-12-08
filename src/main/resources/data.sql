INSERT INTO lectors (name, surname, degree, salary)
VALUES ('First', 'Lector', 'assistant', 1000),
       ('Second', 'Lector', 'associate professor', 2000),
       ('Third', 'Lector', 'professor', 3000);

INSERT INTO departments (name, head)
VALUES ('Applied math', 1),
       ('Physics', 2),
       ('Biology', 3);

INSERT INTO departments_lectors (department_id, lector_id)
VALUES (1, 1),
       (2, 2),
       (3, 3);