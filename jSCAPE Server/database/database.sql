DROP TABLE Student;

CREATE TABLE Student (
    login_name             text NOT NULL PRIMARY KEY,
    first_name             text NOT NULL,
    last_name              text NOT NULL,
	last_login             date,
	last_question_answered date
);

INSERT INTO Student VALUES ('ac6609', 'Alexis', 'Chantreau');
INSERT INTO Student VALUES ('jd4510', 'John', 'Doe');
INSERT INTO Student VALUES ('md1406', 'Matt', 'Damon');

CREATE TABLE Performance (
    login_name         text REFERENCES Student (login_name),
	exercise_category  text NOT NULL,
	questions_answered integer,
	correct_answers    integer,
	wrong_answers      integer
);

INSERT INTO Performance VALUES ('ac6609', 'Global', 440, 277, 163);
INSERT INTO Performance VALUES ('ac6609', 'Arrays', 10, 9, 1);
