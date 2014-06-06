DROP TABLE KnowledgeDistribution;
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
	wrong_answers       integer DEFAULT 0,
    difficulty_category text    DEFAULT 'A1'
);

CREATE TABLE History (
    login_name          text REFERENCES Student (login_name),
    day                 date,
    exercise_category   text REFERENCES Category (exercise_category),
    exercises_answered  integer,
    correct_answers     integer,
    wrong_answers       integer
);

CREATE TABLE ExerciseBank (
    exercise_id       SERIAL NOT NULL PRIMARY KEY,
    exercise_category text REFERENCES Category (exercise_category),
    exercise_text     text NOT NULL,
    correct_answers   integer DEFAULT 0,
    wrong_answers     integer DEFAULT 0,
    --a                 real,
    --b                 real,
    --c                 real DEFAULT 0.25
    difficulty_category text
);

CREATE TABLE StudentExerciseRecord (
    login_name  text REFERENCES Student (login_name),
    exercise_id integer REFERENCES ExerciseBank (exercise_id),
    answer_id   integer
);

CREATE TABLE KnowledgeDistribution (
    login_name        text REFERENCES Student (login_name),
    exercise_category text REFERENCES Category (exercise_category),
    level0             numeric DEFAULT 1/11.0,
    level1             numeric DEFAULT 1/11.0,
    level2             numeric DEFAULT 1/11.0,
    level3             numeric DEFAULT 1/11.0,
    level4             numeric DEFAULT 1/11.0,
    level5             numeric DEFAULT 1/11.0,
    level6             numeric DEFAULT 1/11.0,
    level7             numeric DEFAULT 1/11.0,
    level8             numeric DEFAULT 1/11.0,
    level9             numeric DEFAULT 1/11.0,
    level10            numeric DEFAULT 1/11.0
);

CREATE or REPLACE FUNCTION setup_performance_table() RETURNS trigger AS $setup_performance_table$
    BEGIN
        INSERT INTO Performance(login_name, exercise_category) SELECT login_name, exercise_category FROM Student CROSS JOIN Category WHERE login_name=NEW.login_name;
        RETURN NULL;
    END;
    $setup_performance_table$ LANGUAGE plpgsql;

CREATE or REPLACE FUNCTION setup_knowledge_distribution() RETURNS trigger AS $setup_knowledge_distribution$
    BEGIN
        INSERT INTO KnowledgeDistribution(login_name, exercise_category) SELECT login_name, exercise_category FROM Student CROSS JOIN Category WHERE login_name=NEW.login_name;
        RETURN NULL;
    END;
    $setup_knowledge_distribution$ LANGUAGE plpgsql;


CREATE TRIGGER setup_performance_table
AFTER INSERT ON Student
FOR EACH ROW
EXECUTE PROCEDURE setup_performance_table();

CREATE TRIGGER setup_knowledge_distribution
AFTER INSERT ON Student
FOR EACH ROW
EXECUTE PROCEDURE setup_knowledge_distribution();
