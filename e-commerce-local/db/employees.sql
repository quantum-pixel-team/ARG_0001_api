CREATE TEMP TABLE employees (
    id serial NOT NULL primary key,
    first_name varchar(35),
    last_name varchar(35)
);
INSERT INTO employees(first_name, last_name) VALUES ('Indrit', 'Vaka');
SELECT * FROM employees