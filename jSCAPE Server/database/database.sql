DROP TABLE StudentExerciseRecord;
DROP TABLE ExerciseBank;
DROP TABLE History;
DROP TABLE Performance;
DROP TABLE Category;
DROP TABLE Student;

CREATE TABLE Student (
    login_name             text NOT NULL PRIMARY KEY,
    first_name             text NOT NULL,
    last_name              text NOT NULL,
    class                  text NOT NULL,
	last_login             date,
	last_exercise_answered date,
    password               text NOT NULL
);

CREATE TABLE Category (
    exercise_category text NOT NULL PRIMARY KEY,
    description       text,
    lecture_notes     text,
    helpful_links     text,
    visible           boolean
);

CREATE TABLE Performance (
    login_name          text REFERENCES Student (login_name),
	exercise_category   text REFERENCES Category (exercise_category),
	exercises_answered  integer DEFAULT 0,
	correct_answers     integer DEFAULT 0,
	wrong_answers       integer DEFAULT 0
);

CREATE TABLE History (
    login_name          text REFERENCES Student (login_name),
    day                 date,
    exercises_answered  integer,
    correct_answers     integer,
    wrong_answers       integer
);

CREATE TABLE ExerciseBank (
    exercise_id       SERIAL NOT NULL PRIMARY KEY,
    exercise_category text REFERENCES Category (exercise_category),
    exercise_text     text NOT NULL,
    correct_answers   integer,
    wrong_answers     integer
);

CREATE TABLE StudentExerciseRecord (
    login_name  text REFERENCES Student (login_name),
    exercise_id integer REFERENCES ExerciseBank (exercise_id),
    answer_id   integer
);

CREATE or REPLACE FUNCTION setup_performance_table() RETURNS trigger AS $setup_performance_table$
    BEGIN
        INSERT INTO Performance(login_name, exercise_category) SELECT login_name, exercise_category FROM Student CROSS JOIN Category WHERE login_name=NEW.login_name;
        RETURN NULL;
    END;
    $setup_performance_table$ LANGUAGE plpgsql;


CREATE TRIGGER setup_performance_table
AFTER INSERT ON Student
FOR EACH ROW
EXECUTE PROCEDURE setup_performance_table();

--INSERT INTO Student (login_name, first_name, last_name, last_login, last_exercise_answered) VALUES ('ac6609', 'Alexis', 'Chantreau', '04/05/2014', '04/05/2014');
